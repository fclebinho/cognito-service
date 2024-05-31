package com.clebergomes.aws_cognito.service;

import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesResult;
import com.clebergomes.aws_cognito.requests.ChangePasswordRequest;
import com.clebergomes.aws_cognito.requests.ConfirmSignUpRequest;
import com.clebergomes.aws_cognito.requests.ForgotPasswordRequest;
import com.clebergomes.aws_cognito.requests.ResendConfirmationCodeRequest;
import com.clebergomes.aws_cognito.requests.UpdateUserAttributesRequest;
import com.clebergomes.aws_cognito.requests.UserRegistrationRequest;
import com.clebergomes.aws_cognito.responses.UserLoginResponse;
import com.clebergomes.aws_cognito.responses.UserRegistrationResponse;

public interface UserService {
  UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);

  UserLoginResponse loginUser(String username, String password);

  ConfirmSignUpResult confirmSignUp(ConfirmSignUpRequest request);

  ResendConfirmationCodeResult resendConfirmationCode(ResendConfirmationCodeRequest request);

  UpdateUserAttributesResult updateUserAttributes(String accessToken, UpdateUserAttributesRequest request);

  ChangePasswordResult changePassword(String accessToken, ChangePasswordRequest request);

  ForgotPasswordResult forgotPassword(ForgotPasswordRequest request);
}
