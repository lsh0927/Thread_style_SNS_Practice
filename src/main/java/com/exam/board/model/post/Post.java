package com.exam.board.model.post;


import com.exam.board.model.entity.PostEntity;
import com.exam.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

//여기 값이 널이 아닌 것만
@JsonInclude(JsonInclude.Include.NON_NULL)
public  record Post(Long postId,
                    String body,
                    User user,
                    Long repliesCount,
                    Long likesCount,
                    ZonedDateTime createdDateTime,
                    ZonedDateTime updatedDateTime,
                    ZonedDateTime deletedDateTime,
                    Boolean isLiking
                    )
{

    public static Post from(PostEntity postEntity){
        return new Post(postEntity.getPostId(), postEntity.getBody(), User.from(postEntity.getUser()), postEntity.getRepliesCount(),
                postEntity.getLikesCount(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),postEntity.getDeletedDateTime(), null);
    }
    public static Post from(PostEntity postEntity, boolean isLiking){
        return new Post(postEntity.getPostId(), postEntity.getBody(), User.from(postEntity.getUser()), postEntity.getRepliesCount(),
                postEntity.getLikesCount(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),postEntity.getDeletedDateTime(), isLiking);
    }

}

//
//public class Post {
//
//    private Long postId;
//    private String body;
//    private ZonedDateTime createdDateTime;
//
//
//    public Post(Long postId, String body, ZonedDateTime createdDateTime) {
//        this.postId = postId;
//        this.body = body;
//        this.createdDateTime = createdDateTime;
//    }
//
//
//    public Long getPostId() {
//        return postId;
//    }
//
//    public void setPostId(Long postId) {
//        this.postId = postId;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public ZonedDateTime getCreatedDateTime() {
//        return createdDateTime;
//    }
//
//    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
//        this.createdDateTime = createdDateTime;
//    }
//
//
//
//    //TODO 아래 세개의 메서드를 쓰는 이유?
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Post post = (Post) o;
//        return Objects.equals(postId, post.postId)
//                && Objects.equals(body, post.body)
//                && Objects.equals(createdDateTime, post.createdDateTime);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(postId, body, createdDateTime);
//    }
//
//    @Override
//    public String toString() {
//        return "Post{" +
//                "postId=" + postId +
//                ", body='" + body + '\'' +
//                ", createdDateTime=" + createdDateTime +
//                '}';
//    }
//}
