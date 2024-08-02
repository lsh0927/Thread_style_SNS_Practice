package com.exam.board.model.user;

import com.exam.board.model.entity.UserEntity;

import java.time.ZonedDateTime;


//팔로우 요청 시점까지 또 추가하면 너무 복잡하니까 이 유저를 카피해서 팔로워라는 새 레코드로
public record Follower(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime followedDateTime,
        Boolean isFollowing
) {

    //유저로부터 완성 가능
    public static Follower from(User user, ZonedDateTime followedDateTime){
        return new Follower(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createdDateTime(),
                user.updatedDateTime(),
                followedDateTime,
                user.isFollowing()
        );
    }
}
