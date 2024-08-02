package com.exam.board.service;

import com.exam.board.exception.post.PostNotFoundException;
import com.exam.board.exception.reply.ReplyNotFoundException;
import com.exam.board.exception.user.UserNotAllowedException;
import com.exam.board.exception.user.UserNotFoundException;
import com.exam.board.model.entity.PostEntity;
import com.exam.board.model.entity.ReplyEntity;
import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.post.Post;
import com.exam.board.model.post.PostPatchRequestBody;
import com.exam.board.model.post.PostPostRequestBody;
import com.exam.board.model.reply.Reply;
import com.exam.board.model.reply.ReplyPatchRequestBody;
import com.exam.board.model.reply.ReplyPostRequestBody;
import com.exam.board.repository.PostEntityRepository;
import com.exam.board.repository.ReplyEntityRepository;
import com.exam.board.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyService {


    @Autowired
    private ReplyEntityRepository replyEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    //post list 구성
    private static final List<Post> posts= new ArrayList<>();




    public List<Reply> getRepliesByPostId(Long postId){
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found")
        );

        var replyEntities= replyEntityRepository.findByPost(postEntity);

        return replyEntities.stream().map(Reply::from).toList();
    }


    @Transactional
    public Reply createReply(Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found")
        );


        var replyEntity =replyEntityRepository.save(ReplyEntity.of(replyPostRequestBody.body(),currentUser,postEntity));


        //정합성이 보장되어야 하는 경우: 생성이 되면 이 Count도 무조건 성공하든 둘다 실패해야함
        // 트랜잭션 설정
        postEntity.setRepliesCount(postEntity.getRepliesCount()+1);

        return Reply.from(replyEntity);
    }


    public Reply updateReply(Long postId, Long replyId, ReplyPatchRequestBody replyPatchRequestBody, UserEntity currentUser) {

        //댓글은 포스트와 함께 보여지므로 포스트 확인용이기 때문에 딱히 쓸일은 없으며, 포스트 검증용으로만 사용

        postEntityRepository.findById(postId).orElseThrow(
                ()-> new PostNotFoundException(postId)
        );


        var replyEntity= replyEntityRepository.findById(replyId)
                .orElseThrow(()-> new ReplyNotFoundException(replyId));

        //본인이 아닌 유저가 삭제 및 수정을 못하도록
        if (!replyEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(replyPatchRequestBody.body());

        return Reply.from(replyEntityRepository.save(replyEntity));

    }



    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
         var postEntity=postEntityRepository.findById(postId).orElseThrow(
                ()-> new PostNotFoundException(postId)
        );


        var replyEntity= replyEntityRepository.findById(replyId)
                .orElseThrow(()-> new ReplyNotFoundException(replyId));

        //본인이 아닌 유저가 삭제 및 수정을 못하도록
        if (!replyEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);
        //사람일은 어케 될지 모름
        postEntity.setRepliesCount(Math.max(0,postEntity.getRepliesCount()-1));
        postEntityRepository.save(postEntity);
        //save하는 이유는 중간 집계된 count를 알고싶을때 반영된 결과를 가져오기 위함
    }


    //댓글의 갯수와 좋아요의 갯수를 보여주는 API
    // 중간 집계를 이용하기로 하자
    // PostEntity 수정 ,post record 수정, 댓글 생성 및 저장이 끝난 이후에, postEntity.setRepliesCount()을 기존보다 1증가


    public List<Reply> getRepliesByUser(String username) {
        var userEntity= userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username"+username+"Not found")
        );


        var replyEntities= replyEntityRepository.findByUser(userEntity);

        return replyEntities.stream().map(Reply::from).toList();

    }


}
