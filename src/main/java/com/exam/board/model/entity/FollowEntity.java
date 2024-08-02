package com.exam.board.model.entity;


import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "\"follow\"",
        indexes = {@Index(name = "follow_follower_following_idx", columnList = "follower, following", unique = true)
//                   @Index(name = "reply_postid_idx", columnList = "postid")
        }
)

public class FollowEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    //좋아요 시간순 정렬등을 위함
    @Column
    private ZonedDateTime createdDateTime;



    @ManyToOne
    @JoinColumn(name = "follower")
    private UserEntity follower;


    @ManyToOne
    @JoinColumn(name = "following")
    private UserEntity following;

    public ZonedDateTime getCreatedDateTime() {

        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public UserEntity getFollower() {
        return follower;
    }

    public void setFollower(UserEntity follower) {
        this.follower = follower;
    }

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public UserEntity getFollowing() {
        return following;
    }

    public void setFollowing(UserEntity following) {
        this.following = following;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowEntity that = (FollowEntity) o;
        return Objects.equals(followId, that.followId) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followId, createdDateTime, follower, following);
    }

    public static FollowEntity of(
            UserEntity follower,
            UserEntity following
    ){
        var follow= new FollowEntity();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

    @PrePersist
    private void prePersist(){
        this.createdDateTime=ZonedDateTime.now();
    }


}
