package com.clebergomes.aws_cognito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AuthorizeException extends RuntimeException {

  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

    problemDetail.setTitle("Authorize internal server error");
    problemDetail.setProperty("code", "InternalServerErrorException");

    return problemDetail;
  }
}
