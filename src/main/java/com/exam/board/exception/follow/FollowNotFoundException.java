package com.exam.board.exception.reply;

import com.exam.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends ClientErrorException {

    //Client 에러 메서지 상속
    public ReplyNotFoundException(){
        super(HttpStatus.NOT_FOUND,"Reply not found");
    }

    //만약 postId도 알고 있다면
    public ReplyNotFoundException(Long replyId){
        super(HttpStatus.NOT_FOUND,"Reply with ReplyId"+ replyId + "not found");
    }

    //혹은 발생한 구체적인 메세지를 전달해주고 싶다면
    public ReplyNotFoundException(String message){
        super(HttpStatus.NOT_FOUND,message);
    }
}
