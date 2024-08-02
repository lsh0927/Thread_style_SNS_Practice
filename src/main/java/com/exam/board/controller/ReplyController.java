package com.exam.board.controller;

import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.post.Post;
import com.exam.board.model.post.PostPatchRequestBody;
import com.exam.board.model.post.PostPostRequestBody;
import com.exam.board.model.reply.Reply;
import com.exam.board.model.reply.ReplyPatchRequestBody;
import com.exam.board.model.reply.ReplyPostRequestBody;
import com.exam.board.service.PostService;
import com.exam.board.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")

public class ReplyController {


    @Autowired
    private ReplyService replyService;

   @GetMapping()
   public ResponseEntity<List<Reply>> getRepliesByPostId(
           @PathVariable Long postId
   ){

       var replies= replyService.getRepliesByPostId(postId);

       return ResponseEntity.ok(replies);

       //JSON으로 응답
   }



    @PostMapping()
    public ResponseEntity<Reply> createReply(
            @PathVariable Long postId,
            @RequestBody ReplyPostRequestBody replyPostRequestBody,
            Authentication authentication
    ){

        //userentity로 변환
       var reply =replyService.createReply(postId,replyPostRequestBody,(UserEntity) authentication.getPrincipal());

       return ResponseEntity.ok(reply);


    }


    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
            Authentication authentication
    ){

       var reply= replyService.updateReply(postId,replyId,replyPatchRequestBody,(UserEntity) authentication.getPrincipal());
       return ResponseEntity.ok(reply);
    }


    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            Authentication authentication
    ){

       replyService.deleteReply(postId,replyId,(UserEntity) authentication.getPrincipal());
     //  return ResponseEntity.ok(null);
        return ResponseEntity.noContent().build();
    }


}
