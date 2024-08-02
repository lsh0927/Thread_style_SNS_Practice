package com.exam.board.model.user;

import com.exam.board.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;


//팔로우 요청 시점까지 또 추가하면 너무 복잡하니까 이 유저를 카피해서 팔로워라는 새 레코드로

//좋아요를 누른 시점, 게시물을 포함시킬 새로운 레코드가 또 필요
public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        Boolean isFollowing
) {
    public static User from(UserEntity userEntity){
        return new User(
            userEntity.getUserId(),
            userEntity.getUsername(),
            userEntity.getProfile(),
            userEntity.getDescription(),
                userEntity.getFollowersCount(),
                userEntity.getFollowingsCount(),
            userEntity.getCreatedDateTime(),
            userEntity.getUpdatedDateTime(),
                null
        );
    }
    //상태값 전달을 위해 복사
    //파라미터로 팔로우 여부를 받아야함
    //다음은 userService가서 추가 수정
    public static User from(UserEntity userEntity, boolean isFollowing){
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getFollowersCount(),
                userEntity.getFollowingsCount(),
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime(),
                isFollowing
        );
    }
}
