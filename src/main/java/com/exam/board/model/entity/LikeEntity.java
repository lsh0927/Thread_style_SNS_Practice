package com.exam.board.model.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "\"like\"",
        indexes = {@Index(name = "like_userid_postid_idx", columnList = "userid, postid", unique = true)
//                   @Index(name = "reply_postid_idx", columnList = "postid")
        }
)

public class LikeEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    //좋아요 시간순 정렬등을 위함
    @Column
    private ZonedDateTime createdDateTime;



    //좋아요를 표현하는 userid,postid는 유니크한 값, 실수로라도 좋아요가 2개이상 생성되면 안됨
    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;


    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeEntity that = (LikeEntity) o;
        return Objects.equals(likeId, that.likeId) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(user, that.user) && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeId, createdDateTime, user, post);
    }

    public static LikeEntity of(
            UserEntity user,
            PostEntity post
    ){
        var like= new LikeEntity();
        like.setUser(user);
        like.setPost(post);
        return like;
    }

    @PrePersist
    private void prePersist(){
        this.createdDateTime=ZonedDateTime.now();
    }


}
