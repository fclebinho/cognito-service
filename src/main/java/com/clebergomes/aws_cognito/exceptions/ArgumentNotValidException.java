package com.clebergomes.aws_cognito.exceptions;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ArgumentNotValidException extends AuthorizeException {
  @Override
  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problemDetail.setTitle("Validation failed for argument");
    problemDetail.setType(URI.create("https://example.com/docs/argument-not-valid"));
    problemDetail.setProperty("code", "ArgumentNotValidException");

    return problemDetail;
  }
}
