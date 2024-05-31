package com.clebergomes.aws_cognito.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.clebergomes.aws_cognito.exceptions.AuthorizeException;

@RestControllerAdvice
public class RestExceptHandler {

  @ExceptionHandler(AuthorizeException.class)
  public ProblemDetail handleAuthorizeException(AuthorizeException e) {
    return e.toProblemDetail();
  }
}
