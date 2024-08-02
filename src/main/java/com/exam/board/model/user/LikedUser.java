package com.exam.board.model.user;

import com.exam.board.model.entity.UserEntity;

import java.time.ZonedDateTime;


//팔로우 요청 시점까지 또 추가하면 너무 복잡하니까 이 유저를 카피해서 팔로워라는 새 레코드로

//좋아요를 누른 시점, 게시물을 포함시킬 새로운 레코드가 또 필요
public record LikedUser(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        Boolean isFollowing,
        Long likedPostId,
        ZonedDateTime likedDateTime
) {

    //상태값 전달을 위해 복사
    //파라미터로 팔로우 여부를 받아야함
    //다음은 userService가서 추가 수정
    public static LikedUser from(User user, Long likedPostId, ZonedDateTime likedDateTime){
        return new LikedUser(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createdDateTime(),
                user.updatedDateTime(),
                user.isFollowing(),
                likedPostId,
                likedDateTime
        );
    }
}
