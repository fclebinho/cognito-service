package com.clebergomes.aws_cognito.exceptions;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class CodeMismatchException extends AuthorizeException {
  @Override
  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problemDetail.setTitle("Invalid verification code provided, please try again");
    problemDetail.setType(URI.create("https://example.com/docs/argument-not-valid"));
    problemDetail.setProperty("code", "CodeMismatchException");

    return problemDetail;
  }
}
