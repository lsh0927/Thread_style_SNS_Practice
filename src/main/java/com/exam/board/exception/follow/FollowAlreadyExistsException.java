package com.exam.board.exception.follow;

import com.exam.board.exception.ClientErrorException;
import com.exam.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowAlreadyExistsException extends ClientErrorException {

    //Client 에러 메서지 상속
    public FollowAlreadyExistsException(){
        super(HttpStatus.CONFLICT,"Follow already exists");
    }

    public FollowAlreadyExistsException(UserEntity follower, UserEntity following){
        super(HttpStatus.CONFLICT,"Follow with follower"+ follower.getUsername()
                + "and following " + following.getUsername() +  "already exists");
    }


}
