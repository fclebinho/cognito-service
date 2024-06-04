package com.clebergomes.aws_cognito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;

public class AWSCognitoIdentityProviderException extends AuthorizeException {

  @Nullable
  private String detail;

  public AWSCognitoIdentityProviderException(String detail) {
    this.detail = detail;
  }

  public ProblemDetail commonProblemDetail(String detail) {
    String[] errorMessages = detail.replaceAll("\\)", "").split("\\(");
    String[] titles = errorMessages[0].split(":");

    String[] details = errorMessages[1].split(";\\s");

    var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

    problemDetail.setTitle(titles[0]);
    problemDetail.setDetail(titles[1]);

    String[] service = details[0].split(":");
    String[] statusCode = details[1].split(":");
    String[] errorCode = details[2].split(":");

    problemDetail.setProperty("service", service[1].trim());
    problemDetail.setProperty("error", errorCode[1].trim());
    problemDetail.setProperty("status", Integer.parseInt(statusCode[1].trim()));

    return problemDetail;
  }

  @Override
  public ProblemDetail toProblemDetail() {

    return commonProblemDetail(detail);
  }

}
