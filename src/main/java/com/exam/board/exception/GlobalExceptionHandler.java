package com.exam.board.exception;

import com.exam.board.model.error.ClientErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//시큐리티 관련 필터는 여기서 못잡음, 따라서 추가 필터 필요
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException.class)
    //클라이언트 에러 예외를 핸들링할 함수
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(ClientErrorException e){
        //핸들링할 예외의 클래스 정보를 넘겨줘야함

        //이제 이 함수에서 예외를 받아 http response를 단들건데, 아까 그 dto를 활용할것임: ClientErrorResponse

        return new ResponseEntity<>(new ClientErrorResponse(e.getStatus(),e.getMessage()),e.getStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    //잘못된 형식
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(MethodArgumentNotValidException e){
        //핸들링할 예외의 클래스 정보를 넘겨줘야함
        var errorMessage=e.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": "+ fieldError.getDefaultMessage()).toList().toString();
        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    //요구되는 request body가 빠져있을 떄
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(HttpMessageNotReadableException e){
        //핸들링할 예외의 클래스 정보를 넘겨줘야함

        //이제 이 함수에서 예외를 받아 http response를 단들건데, 아까 그 dto를 활용할것임: ClientErrorResponse

        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    //클라이언트 에러 예외를 핸들링할 함수
    public ResponseEntity<ClientErrorResponse> handleRuntimeException(RuntimeException e){
        return ResponseEntity.internalServerError().build();
    }


    @ExceptionHandler(Exception.class)
    //클라이언트 에러 예외를 핸들링할 함수
    public ResponseEntity<ClientErrorResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError().build();

    }

}