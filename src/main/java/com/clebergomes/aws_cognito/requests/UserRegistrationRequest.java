package com.clebergomes.aws_cognito.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationRequest {

  @NotBlank(message = "Full name is required")
  private String fullName;

  @Email(message = "Invalid email address")
  private String email;

  @NotBlank(message = "Phone number is required")
  private String phoneNumber;

  @NotBlank(message = "Password is required")
  private String password;
}
