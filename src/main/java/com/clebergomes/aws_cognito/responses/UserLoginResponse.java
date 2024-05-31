package com.clebergomes.aws_cognito.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponse {
  private String access_token;
  private Integer expires_in;
  private String token_type;
  private String refresh_token;
  private String id_token;
}
