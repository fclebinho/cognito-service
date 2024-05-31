package com.clebergomes.aws_cognito.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserRegistrationResponse {
  private UUID id;
  private String fullName;
  private String email;
  private String phoneNumber;
  private Boolean isVerified;
}
