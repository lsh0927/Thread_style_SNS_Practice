package com.exam.board.service;

import com.exam.board.exception.post.PostNotFoundException;
import com.exam.board.exception.user.UserNotAllowedException;
import com.exam.board.exception.user.UserNotFoundException;
import com.exam.board.model.entity.LikeEntity;
import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.post.Post;
import com.exam.board.model.post.PostPatchRequestBody;
import com.exam.board.model.post.PostPostRequestBody;
import com.exam.board.model.entity.PostEntity;
import com.exam.board.model.user.User;
import com.exam.board.repository.LikeEntityRepository;
import com.exam.board.repository.PostEntityRepository;
import com.exam.board.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    //게시물에 좋아요를 처음 누른건지 아닌지 확인용
    @Autowired
    private LikeEntityRepository likeEntityRepository;

    //post list 구성
    private static final List<Post> posts= new ArrayList<>();


    public List<Post> getPosts(UserEntity currentUser){
     //   return posts;
        var postEntities = postEntityRepository.findAll();

        //return postEntities;
        //DB의 모든 데이터를 통으로 주지 말고, 다른 Dto로 바꾸자
        //그게 기존의 Post
        return postEntities.stream().map(
                postEntity -> getPostWithLikingStatus(postEntity,currentUser)
        ).toList();

    }

    public Post getPostByPostId(Long postId, UserEntity currentUser){

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found")
        );
//
//        var isLiking= likeEntityRepository.findByUserAndPost(currentUser,postEntity).isPresent();

     //   return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

  //      return Post.from(postEntity,isLiking);

        return getPostWithLikingStatus(postEntity,currentUser);
    }


    private Post getPostWithLikingStatus(PostEntity postEntity,UserEntity currentUser){
        var isLiking= likeEntityRepository.findByUserAndPost(currentUser,postEntity).isPresent();
        return Post.from(postEntity,isLiking);
    }


    //post를 새로 생성, repo에 save도 필요
    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {
        //내가 보기에, 여기서 postentity of 메서드가 필요할거라 생각함

//        var postEntity= new PostEntity();
//        postEntity.setBody(postPostRequestBody.body());
//
//        //가져온 유저 정보 저장
//        postEntity.setUser(currentUser);
        var postEntity=PostEntity.of(postPostRequestBody.body(), currentUser);

        postEntityRepository.save(postEntity);
        //바디만 설정했었음

        return Post.from(postEntity);
    }

    // 수정과 삭제: 첫 등록한 유저와, 현재 currentUser가 같을때에만 허용
    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {
        //단건조회의 로직 그대로 가져와

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new PostNotFoundException(postId)
        );

        if (!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        postEntity.setBody(postPatchRequestBody.body());


        var updatePostEntity=postEntityRepository.save(postEntity);
        return Post.from(updatePostEntity);

    }

    public void deletePost(Long postId,UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new PostNotFoundException(postId)
        );

        if (!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostByUsername(String username, UserEntity currentUser) {
        var userEntity= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        var postEntities= postEntityRepository.findByUser(userEntity);

   //     return postEntities.stream().map(Post::from).toList();
        return postEntities.stream().map(postEntity -> getPostWithLikingStatus(postEntity,currentUser)).toList();
    }



    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new PostNotFoundException(postId)
        );

        var likeEntity= likeEntityRepository.findByUserAndPost(currentUser,postEntity);

        if (likeEntity.isPresent()){
            //존재한다면 삭제 (버튼을 한번 누르면 하트, 두번누르면 삭제
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0,postEntity.getLikesCount()-1));
            return Post.from(postEntityRepository.save(postEntity),false);
        }else {
            //새롭게 생성
            likeEntityRepository.save(LikeEntity.of(currentUser,postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount()+1);
            return Post.from(postEntityRepository.save(postEntity),true);
        }
        //return Post.from(postEntity);
        //return Post.from(postEntityRepository.save(postEntity));
    }
}
