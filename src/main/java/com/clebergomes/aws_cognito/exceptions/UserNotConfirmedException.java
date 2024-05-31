package com.clebergomes.aws_cognito.exceptions;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotConfirmedException extends AuthorizeException {
  @Override
  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problemDetail.setTitle("User is not confirmed");
    problemDetail.setType(URI.create("https://example.com/docs/user-not-confirmed"));
    problemDetail.setProperty("code", "UserNotConfirmedException");

    return problemDetail;
  }
}
