package com.clebergomes.aws_cognito.service;

import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesResult;
import com.clebergomes.aws_cognito.requests.UserRegistrationRequest;
import com.clebergomes.aws_cognito.responses.UserLoginResponse;
import com.clebergomes.aws_cognito.responses.UserRegistrationResponse;

public interface UserService {
  UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);

  UserLoginResponse loginUser(String username, String password);

  ConfirmSignUpResult confirmSignUp(String email, String confirmationCode);

  ResendConfirmationCodeResult resendConfirmationCode(String email);

  UpdateUserAttributesResult updateUserAttributes(String accessToken, String email, String phoneNumber,
      String fullName);

  ChangePasswordResult changePassword(String accessToken, String previousPassword, String proposedPassword);

  ForgotPasswordResult forgotPassword(String email);
}
