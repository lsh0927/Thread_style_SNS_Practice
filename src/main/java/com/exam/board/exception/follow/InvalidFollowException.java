package com.exam.board.exception.follow;

import com.exam.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class InvalidFollowException extends ClientErrorException {

    //Client 에러 메서지 상속
    public InvalidFollowException(){
        super(HttpStatus.BAD_REQUEST,"Invalid follow request");
    }

    public InvalidFollowException(String message){
        super(HttpStatus.BAD_REQUEST,message);
    }

}
