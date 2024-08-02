package com.exam.board.service;

import com.exam.board.exception.follow.FollowAlreadyExistsException;
import com.exam.board.exception.follow.FollowNotFoundException;
import com.exam.board.exception.follow.InvalidFollowException;
import com.exam.board.exception.user.UserAlreadyExistsException;
import com.exam.board.exception.user.UserNotAllowedException;
import com.exam.board.exception.user.UserNotFoundException;
import com.exam.board.model.entity.FollowEntity;
import com.exam.board.model.entity.LikeEntity;
import com.exam.board.model.entity.PostEntity;
import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.user.*;
import com.exam.board.repository.FollowEntityRepository;
import com.exam.board.repository.LikeEntityRepository;
import com.exam.board.repository.PostEntityRepository;
import com.exam.board.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FollowEntityRepository followEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private LikeEntityRepository likeEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );
    }


    public User signup(String username, String password) {
     userEntityRepository.findByUsername(username)
                .ifPresent(
                user -> {throw  new UserAlreadyExistsException();
                }
        );

     //회원가입에 of가 쓰임
        var userEntity = UserEntity.of(username, bCryptPasswordEncoder.encode(password));
        userEntityRepository.save(userEntity);

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        var userEntity= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        if(passwordEncoder.matches(password,userEntity.getPassword())){
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        }else {
            throw new UserNotFoundException();
        }


    }


    public List<User> getUsers(String query, UserEntity currentUser) {

        List<UserEntity> userEntities;

        if (query!=null && !query.isBlank()){
            //TODO: query 검색어 기반, 해당 검색어가 username에 포함되어 있는 유저목록 가져오기
            userEntities=userEntityRepository.findByUsernameContaining(query);
        }else {
            userEntities=userEntityRepository.findAll();
        }
    //    return userEntities.stream().map(User::from).toList();
        return userEntities.stream().map(userEntity -> getUserWithFollowingStatus(userEntity,currentUser)).toList();


    }

    public User getUser(String username,UserEntity currentUser) {
        var userEntity= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

//        var isFollowing =followEntityRepository.findByFollowerAndFollowing(currentUser,userEntity).isPresent();
//
//        return User.from(userEntity,isFollowing);

        return getUserWithFollowingStatus(userEntity,currentUser);

    }

    private User getUserWithFollowingStatus(UserEntity userEntity, UserEntity currentUser){
        var isFollowing =followEntityRepository.findByFollowerAndFollowing(currentUser,userEntity).isPresent();

        return User.from(userEntity,isFollowing);
    }


    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        var userEntity= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        if (!userEntity.equals(currentUser)){
            throw new UserNotAllowedException();
        }
        userEntity.setDescription(userPatchRequestBody.description());
        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        var following= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        //본인이 본인을 팔로우할수는 없음

        if (following.equals(currentUser)){
            throw  new InvalidFollowException("A user cannot follow themselves");
        }

        //팔로우 추가 로직 -> 중복 방지
        followEntityRepository.findByFollowerAndFollowing(currentUser,following)
                .ifPresent(
                        follow-> {
                            throw new FollowAlreadyExistsException(currentUser,following);
                        }
                );


        followEntityRepository.save(
                FollowEntity.of(currentUser,following)
        );

        following.setFollowersCount(following.getFollowersCount()+1);

        currentUser.setFollowingsCount(currentUser.getFollowingsCount()+1);

//        userEntityRepository.save(following);
//        userEntityRepository.save(currentUser);


        userEntityRepository.saveAll(List.of(following,currentUser));
        //처리가 되었으므로
        return User.from(following,true);



        //근데 팔로워 팔로잉 수가 필요
    }


    @Transactional
    public User unfollow(String username, UserEntity currentUser) {

        var following= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        //본인이 본인을 팔로우할수는 없음

        if (following.equals(currentUser)){
            throw  new InvalidFollowException("A user cannot unfollow themselves");
        }

        var followEntity= followEntityRepository.findByFollowerAndFollowing(currentUser,following).orElseThrow(
                () -> {
                    throw  new FollowNotFoundException(currentUser,following);
                }
        );

        followEntityRepository.delete(followEntity);


        following.setFollowersCount(Math.max(0,following.getFollowersCount()-1));

        currentUser.setFollowingsCount(Math.max(0,currentUser.getFollowingsCount()-1));

//        userEntityRepository.save(following);
//        userEntityRepository.save(currentUser);


        userEntityRepository.saveAll(List.of(following,currentUser));
        //처리가 되었으므로


        //유저 컨트롤러가서 APi 수정
        return User.from(following,false);



        //근데 팔로워 팔로잉 수가 필요
    }

    //허허...
    public List<Follower> getFollowersByUsername(String username, UserEntity currentUser) {
        var following= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        //모두 동일한 팔로잉
        var followEntities= followEntityRepository.findByFollowing(following);



//        return followEntities.stream().map(
//                follow ->User.from(follow.getFollower())).toList();

        return followEntities.stream().map
                (follow-> Follower.from(
                        getUserWithFollowingStatus(follow.getFollower(),currentUser),follow.getCreatedDateTime()))
                        .toList();
    }

    public List<User> getFollowingsByUsername(String username,UserEntity currentUser) {

        var follower= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        //모두 동일한 팔로잉
        var followEntities= followEntityRepository.findByFollower(follower);

//        return followEntities.stream().map(follow ->User.from(follow.getFollowing())).toList();

        return followEntities.stream().map(follow -> getUserWithFollowingStatus(follow.getFollowing(),currentUser)).toList();
    }


    //난이도 돌았네 ㅋㅋㅋㅌ
    public List<LikedUser> getLikedUsersByPostId(Long postId, UserEntity currentUser) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found")
        );



        var likeEntities= likeEntityRepository.findByPost(postEntity);

        return likeEntities.stream().map(
                likeEntity ->

                    //로직이 복잡함
//                    var likedUserEntity = likeEntity.getUser();
//                    var userWithFollowingStatus=
//                        getUserWithFollowingStatus(likedUserEntity,currentUser);
//
//                    return LikedUser.from(userWithFollowingStatus,postEntity.getPostId(),likeEntity.getCreatedDateTime());
                    getLikedUserWithFollowingStatus(likeEntity,postEntity,currentUser)).toList();
    }

    private LikedUser getLikedUserWithFollowingStatus(LikeEntity likeEntity, PostEntity postEntity, UserEntity currentUser){
        var likedUserEntity = likeEntity.getUser();
        var userWithFollowingStatus=
                getUserWithFollowingStatus(likedUserEntity,currentUser);

        return LikedUser.from(userWithFollowingStatus,postEntity.getPostId(),likeEntity.getCreatedDateTime());
    }

    // 진짜 현업에서는 이렇게 안함
    // 개복잡하긴한데 다시 한번 해보자
    public List<LikedUser> getLikedUsersByUser(String username, UserEntity currentUser) {
        var userEntity= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        var postEntities= postEntityRepository.findByUser(userEntity);
        return postEntities.stream().flatMap(
                postEntity->
                     likeEntityRepository.findByPost(postEntity).stream().map(
                             likeEntity -> getLikedUserWithFollowingStatus(likeEntity,postEntity,currentUser))
        ).toList();


    }
}
