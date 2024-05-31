package com.clebergomes.aws_cognito.service;

import com.clebergomes.aws_cognito.requests.UserRegistrationRequest;
import com.clebergomes.aws_cognito.responses.UserLoginResponse;
import com.clebergomes.aws_cognito.responses.UserRegistrationResponse;

public interface UserService {
  UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);

  UserLoginResponse loginUser(String username, String password);
}
