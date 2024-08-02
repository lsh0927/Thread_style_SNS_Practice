package com.exam.board.controller;
import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.post.Post;

import com.exam.board.model.post.PostPatchRequestBody;
import com.exam.board.model.post.PostPostRequestBody;
import com.exam.board.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")

public class PostController {

    private static final Logger  logger= LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

   @GetMapping()
   public ResponseEntity<List<Post>> getPosts(){

       logger.info("GET /api/v1/posts");
       List<Post> posts= postService.getPosts();


       return ResponseEntity.ok(posts);
       //JSON으로 응답
   }


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(
            @PathVariable Long postId
    ){

        logger.info("GET /api/v1/posts{}",postId);

        var post =
              postService.getPostByPostId(postId);

//        return matchingPost.map(ResponseEntity::ok)
//                .orElseGet(()-> ResponseEntity.notFound().build());

        return ResponseEntity.ok(post);
        //훨씬더 간결해짐
        //JSON으로 응답
    }


    @PostMapping()
    public ResponseEntity<Post> createPost(
            @RequestBody PostPostRequestBody postPostRequestBody,
            Authentication authentication
    ){
        logger.info("Post /api/v1/posts{}");

        //userentity로 변환
       Post post= postService.createPost(postPostRequestBody, (UserEntity) authentication.getPrincipal());

       return ResponseEntity.ok(post);


    }



    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody postPatchRequestBody,
            Authentication authentication
    ){
        logger.info("PATCH /api/v1/posts{}",postId);

       var post= postService.updatePost(postId,postPatchRequestBody,(UserEntity) authentication.getPrincipal());
       return ResponseEntity.ok(post);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication
    ){
        logger.info("DELETE /api/v1/posts{}",postId);

       postService.deletePost(postId,(UserEntity) authentication.getPrincipal());
     //  return ResponseEntity.ok(null);
        return ResponseEntity.noContent().build();
    }


}
