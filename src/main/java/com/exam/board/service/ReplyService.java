package com.exam.board.service;

import com.exam.board.exception.post.PostNotFoundException;
import com.exam.board.exception.user.UserNotAllowedException;
import com.exam.board.exception.user.UserNotFoundException;
import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.post.Post;
import com.exam.board.model.post.PostPatchRequestBody;
import com.exam.board.model.post.PostPostRequestBody;
import com.exam.board.model.entity.PostEntity;
import com.exam.board.repository.PostEntityRepository;
import com.exam.board.repository.UserEntityRepository;
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

    //post list 구성
    private static final List<Post> posts= new ArrayList<>();


    public List<Post> getPosts(){
     //   return posts;
        var postEntities = postEntityRepository.findAll();

        //return postEntities;
        //DB의 모든 데이터를 통으로 주지 말고, 다른 Dto로 바꾸자
        //그게 기존의 Post
        return postEntities.stream().map(Post::from).toList();

    }

    public Post getPostByPostId(Long postId){

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found")
        );

     //   return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        return Post.from(postEntity);
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

    public List<Post> getPostByUsername(String username) {
        var userEntity= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );

        var postEntities= postEntityRepository.findByUser(userEntity);

        return postEntities.stream().map(Post::from).toList();
    }
}
