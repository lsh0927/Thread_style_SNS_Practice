package com.exam.board.exception.user;

import com.exam.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {

    //Client 에러 메서지 상속
    public UserNotAllowedException(){
        super(HttpStatus.FORBIDDEN,"User not allowed");
    }



}
