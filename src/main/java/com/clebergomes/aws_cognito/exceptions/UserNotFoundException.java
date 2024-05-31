package com.clebergomes.aws_cognito.exceptions;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundException extends AuthorizeException {
  @Override
  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problemDetail.setTitle("User does not exist");
    problemDetail.setType(URI.create("https://example.com/docs/user-not-exist"));
    problemDetail.setProperty("code", "UserNotFoundException");

    return problemDetail;
  }
}
