package com.clebergomes.aws_cognito.exceptions;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AccountAlreadyConfirmedException extends AuthorizeException {
  @Override
  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problemDetail.setTitle("User cannot be confirmed");
    problemDetail.setDetail("Current status is CONFIRMED");
    problemDetail.setType(URI.create("https://example.com/docs/account-already-confirmed"));
    problemDetail.setProperty("code", "UserNotConfirmedException");

    return problemDetail;
  }
}
