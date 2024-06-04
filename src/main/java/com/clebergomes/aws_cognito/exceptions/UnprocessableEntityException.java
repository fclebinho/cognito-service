package com.clebergomes.aws_cognito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;

public class UnprocessableEntityException extends AuthorizeException {

  @Nullable
  private String detail;

  public UnprocessableEntityException(String detail) {
    this.detail = detail;
  }

  public UnprocessableEntityException() {
    this.detail = null;
  }

  @Override
  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

    problemDetail.setTitle("Unprocessable entity");
    problemDetail.setDetail(detail);
    problemDetail.setProperty("code", "UnprocessableEntityException");

    return problemDetail;
  }

}
