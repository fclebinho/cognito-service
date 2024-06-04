package com.clebergomes.aws_cognito.exceptions;

import org.springframework.http.ProblemDetail;

import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;

public class AuthorizeException extends AWSCognitoIdentityProviderException {

  public AuthorizeException(String message) {
    super(message);
  }

  public AuthorizeException(AWSCognitoIdentityProviderException exception) {
    super(exception.getErrorMessage());

    this.setErrorCode(exception.getErrorCode());
    this.setStatusCode(exception.getStatusCode());
  }

  public ProblemDetail toProblemDetail() {
    var problemDetail = ProblemDetail.forStatus(this.getStatusCode());
    problemDetail.setTitle(this.getErrorMessage());
    problemDetail.setProperty("code", this.getErrorCode());

    return problemDetail;
  }
}
