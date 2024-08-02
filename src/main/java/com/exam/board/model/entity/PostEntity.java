package com.exam.board.model.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "post",
        indexes = {@Index(name = "post_userid_idx", columnList = "userid")}
)

@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
// Deprecated in Hibernate 6.3
// @Where(clause = "deletedDateTime IS NULL")

@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;


    @Column(nullable = false)
    private String body;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    @Column
    private Long repliesCount=0L;

    @Column
    private Long likesCount=0L;


    //연관관계 매핑
    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;
    //post를 통해 유저 조회, 유저의 게시물 조회 쿼리 성능 향상을 위한 어노테이션 필요


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(postId, that.postId) && Objects.equals(body, that.body) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(updatedDateTime, that.updatedDateTime) && Objects.equals(deletedDateTime, that.deletedDateTime) && Objects.equals(repliesCount, that.repliesCount) && Objects.equals(likesCount, that.likesCount) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime, updatedDateTime, deletedDateTime, repliesCount, likesCount, user);
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

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Long repliesCount) {
        this.repliesCount = repliesCount;
    }

    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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


    //롬복은 왜 안쓰나? java가 아니라 코틀린을 쓰면 쓸일이 없다는 강사의 말
    public static PostEntity of(
            String body,
            UserEntity user
    ){
        var post= new PostEntity();
        post.setBody(body);
        post.setUser(user);
        return post;
    }


    // post record도 수정이 필요
}
