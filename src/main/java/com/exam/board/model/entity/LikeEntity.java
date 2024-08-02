package com.exam.board.model.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "Reply",
        indexes = {@Index(name = "reply_userid_idx", columnList = "userid"),
                   @Index(name = "reply_postid_idx", columnList = "postid")
        }
)

@SQLDelete(sql = "UPDATE \"reply\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE replyㅑㅇ = ?")
// Deprecated in Hibernate 6.3
// @Where(clause = "deletedDateTime IS NULL")

@SQLRestriction("deleteddatetime IS NULL")
public class ReplyEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;


    @Column(nullable = false)
    private String body;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    //연관관계 매핑
    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;


    //연관관계 매핑
    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getDeletedDateTime() {
        return deletedDateTime;
    }

    public void setDeletedDateTime(ZonedDateTime deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }


    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplyEntity reply = (ReplyEntity) o;
        return Objects.equals(replyId, reply.replyId) && Objects.equals(body, reply.body) && Objects.equals(createdDateTime, reply.createdDateTime) && Objects.equals(updatedDateTime, reply.updatedDateTime) && Objects.equals(deletedDateTime, reply.deletedDateTime) && Objects.equals(user, reply.user) && Objects.equals(post, reply.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyId, body, createdDateTime, updatedDateTime, deletedDateTime, user, post);
    }

    public static ReplyEntity of(
            String body,
            UserEntity user,
            PostEntity post
    ){
        var reply= new ReplyEntity();
        reply.setBody(body);
        reply.setUser(user);
        reply.setPost(post);
        return reply;
    }

    @PrePersist
    private void prePersist(){
        this.createdDateTime=ZonedDateTime.now();
        this.updatedDateTime=this.createdDateTime;
    }

    //수정 직전에 편리하게 기능 추가 가능
    @PreUpdate
    private void preUpdate(){
         this.updatedDateTime=ZonedDateTime.now();
    }
}
