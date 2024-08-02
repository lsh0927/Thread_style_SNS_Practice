package com.exam.board.exception.user;

import com.exam.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClientErrorException {

    //Client 에러 메서지 상속
    public UserNotFoundException(){
        super(HttpStatus.NOT_FOUND,"User not found");
    }

    public UserNotFoundException(String username){
        super(HttpStatus.NOT_FOUND,"User with username"+ username + "not found");
    }


}
