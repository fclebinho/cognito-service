package com.clebergomes.aws_cognito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesResult;
import com.clebergomes.aws_cognito.model.User;
import com.clebergomes.aws_cognito.repository.UserRepository;
import com.clebergomes.aws_cognito.requests.UserRegistrationRequest;
import com.clebergomes.aws_cognito.responses.UserLoginResponse;
import com.clebergomes.aws_cognito.responses.UserRegistrationResponse;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private CognitoService cognitoService;

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserRegistrationResponse registerUser(UserRegistrationRequest input) {
    // Register the user with Amazon Cognito
    try {
      UserRegistrationResponse userRegistrationResponse = cognitoService.registerUser(input);

      User registeredUser = new User();
      registeredUser.setPhoneNumber(input.getPhoneNumber());
      registeredUser.setEmail(input.getEmail());
      registeredUser.setFullName(input.getFullName());

      User newUser = userRepository.save(registeredUser);

      cognitoService.resendConfirmationCode(input.getEmail());
      userRegistrationResponse.setId(newUser.getId());

      return userRegistrationResponse;
    } catch (Exception e) {
      throw new RuntimeException("Error registering user", e);
    }
  }

  @Override
  public UserLoginResponse loginUser(String email, String password) {
    return cognitoService.loginUser(email, password);
  }

  @Override
  public ConfirmSignUpResult confirmSignUp(String email, String confirmationCode) {
    return cognitoService.confirmSignUp(email, confirmationCode);
  }

  @Override
  public ResendConfirmationCodeResult resendConfirmationCode(String email) {
    return cognitoService.resendConfirmationCode(email);
  }

  @Override
  public UpdateUserAttributesResult updateUserAttributes(String accessToken, String email, String phoneNumber,
      String fullName) {
    return cognitoService.updateUserAttributes(accessToken, email, phoneNumber, fullName);
  }

  @Override
  public ChangePasswordResult changePassword(String accessToken, String previousPassword, String proposedPassword) {
    return cognitoService.changePassword(accessToken, previousPassword, proposedPassword);
  }

  @Override
  public ForgotPasswordResult forgotPassword(String email) {
    return cognitoService.forgotPassword(email);
  }

}
