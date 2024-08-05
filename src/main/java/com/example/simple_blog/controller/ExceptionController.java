package com.example.simple_blog.controller;


import com.example.simple_blog.exception.member.join.DuplicateException;
import com.example.simple_blog.exception.member.join.InvalidException;
import com.example.simple_blog.exception.member.login.LoginException;
import com.example.simple_blog.exception.post.PostException;
import com.example.simple_blog.exception.token.TokenException;
import com.example.simple_blog.dto.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = getErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST), "유효하지 않는 값입니다.");
        errorResponse.addValidation(ex.getFieldErrors());
        return  errorResponse;
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenException.class)
    public ErrorResponse invalidRequestHandler(TokenException ex) {
        return getErrorResponse(String.valueOf(ex.statusCode()), ex.getMessage());
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateException.class)
    public ErrorResponse invalidRequestHandler(DuplicateException ex) {
        return getErrorResponse(String.valueOf(ex.statusCode()), ex.getMessage());
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidException.class)
    public ErrorResponse invalidRequestHandler(InvalidException ex) {
        return getErrorResponse(String.valueOf(ex.statusCode()), ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(LoginException.class)
    public HttpEntity<ErrorResponse> invalidRequestHandler(LoginException ex) {
        ErrorResponse errorResponse = getErrorResponse(String.valueOf(ex.statusCode()), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(PostException.class)
    public HttpEntity<String> invalidRequestHandler(PostException ex) {
        return ResponseEntity.status(ex.statusCode()).body(ex.getMessage());
    }





    private ErrorResponse getErrorResponse(String statusCode, String message) {
        return ErrorResponse.builder()
                .code(statusCode)
                .message(message)
                .build();
    }


}
