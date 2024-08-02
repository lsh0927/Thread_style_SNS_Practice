package com.exam.board.model.reply;


import com.exam.board.model.entity.ReplyEntity;
import com.exam.board.model.post.Post;
import com.exam.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

//여기 값이 널이 아닌 것만
@JsonInclude(JsonInclude.Include.NON_NULL)
public  record Reply(Long replyId,
                     String body,
                     User user,

                     Post post,

                     ZonedDateTime createdDateTime,
                     ZonedDateTime updatedDateTime,
                     ZonedDateTime deletedDateTime

                     //post와의 차이

){

    public static Reply from(ReplyEntity replyEntity){

        // TODO user.from와 post.from이 왜 필요했더라??
        return new Reply(replyEntity.getReplyId(), replyEntity.getBody(), User.from(replyEntity.getUser()),
                Post.from(replyEntity.getPost()),replyEntity.getCreatedDateTime(),
                replyEntity.getUpdatedDateTime(),replyEntity.getDeletedDateTime());
    }

}
