package com.exam.board.exception.follow;

import com.exam.board.exception.ClientErrorException;
import com.exam.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowNotFoundException extends ClientErrorException {

    //Client 에러 메서지 상속
    public FollowNotFoundException(){
        super(HttpStatus.NOT_FOUND,"Follow not found");
    }


    //혹은 발생한 구체적인 메세지를 전달해주고 싶다면
    public FollowNotFoundException(UserEntity follower, UserEntity following){
        super(HttpStatus.NOT_FOUND,"Follow with follower"+ follower.getUsername()
                + "and following " + following.getUsername() +  "already exists");
    }

}
