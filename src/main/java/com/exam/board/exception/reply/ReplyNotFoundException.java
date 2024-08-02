package com.exam.board.exception.post;

import com.exam.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ClientErrorException {

    //Client 에러 메서지 상속
    public PostNotFoundException(){
        super(HttpStatus.NOT_FOUND,"Post not found");
    }

    //만약 postId도 알고 있다면
    public PostNotFoundException(Long postId){
        super(HttpStatus.NOT_FOUND,"Post with postId"+ postId + "not found");
    }

    //혹은 발생한 구체적인 메세지를 전달해주고 싶다면
    public PostNotFoundException(String message){
        super(HttpStatus.NOT_FOUND,message);
    }
}
