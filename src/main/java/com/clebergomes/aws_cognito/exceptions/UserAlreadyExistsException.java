package com.clebergomes.aws_cognito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;

public class UserAlreadyExistsException extends AuthorizeException {

  @Nullable
  private String detail;

  public UserAlreadyExistsException(String detail) {
    this.detail = detail;
  }

  public UserAlreadyExistsException() {
    this.detail = null;
  }

  @Override
  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

    problemDetail.setTitle("User already exists");
    problemDetail.setDetail(detail);

    return problemDetail;
  }

}
