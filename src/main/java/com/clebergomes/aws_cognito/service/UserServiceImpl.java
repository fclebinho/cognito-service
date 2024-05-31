package com.clebergomes.aws_cognito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clebergomes.aws_cognito.requests.UserRegistrationRequest;
import com.clebergomes.aws_cognito.responses.UserLoginResponse;
import com.clebergomes.aws_cognito.responses.UserRegistrationResponse;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private CognitoService cognitoService;

  @Override
  public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
    // Register the user with Amazon Cognito
    return cognitoService.registerUser(userRegistrationRequest);
  }

  @Override
  public UserLoginResponse loginUser(String email, String password) {
    return cognitoService.loginUser(email, password);
  }

}
