package com.clebergomes.aws_cognito.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesResult;
import com.clebergomes.aws_cognito.exceptions.AWSCognitoIdentityProviderException;
import com.clebergomes.aws_cognito.exceptions.UnprocessableEntityException;
import com.clebergomes.aws_cognito.model.User;
import com.clebergomes.aws_cognito.repository.UserRepository;
import com.clebergomes.aws_cognito.requests.ChangePasswordRequest;
import com.clebergomes.aws_cognito.requests.ConfirmSignUpRequest;
import com.clebergomes.aws_cognito.requests.ForgotPasswordRequest;
import com.clebergomes.aws_cognito.requests.ResendConfirmationCodeRequest;
import com.clebergomes.aws_cognito.requests.UpdateUserAttributesRequest;
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
  public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
    System.out.println("*** request ***");
    System.out.println(request);
    // Register the user with Amazon Cognito
    try {
      UserRegistrationResponse response = cognitoService.registerUser(request);

      System.out.println("*** response ***");
      System.out.println(response);

      User user = findOrCreateUser(request);
      System.out.println("*** user ***");
      System.out.println(user);

      CompletableFuture.runAsync(() -> cognitoService.resendConfirmationCode(request.getEmail()));

      response.setId(user.getId());
      return response;
    } catch (Exception e) {

      if (e.toString().contains("AWSCognitoIdentityProvider")) {
        if (e.toString().contains("UsernameExistsException")) {
          CompletableFuture.runAsync(() -> findOrCreateUser(request));
        }

        throw new AWSCognitoIdentityProviderException(e.getMessage());
      }

      throw new UnprocessableEntityException(e.getMessage());
    }
  }

  private User findOrCreateUser(UserRegistrationRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      return userRepository.findByEmail(request.getEmail()).get();
    }

    User registeredUser = new User();
    registeredUser.setPhoneNumber(request.getPhoneNumber());
    registeredUser.setEmail(request.getEmail());
    registeredUser.setFullName(request.getFullName());

    return userRepository.save(registeredUser);
  }

  @Override
  public UserLoginResponse loginUser(String email, String password) {
    try {
      return cognitoService.loginUser(email, password);
    } catch (Exception e) {
      if (e.toString().contains("AWSCognitoIdentityProvider")) {
        throw new AWSCognitoIdentityProviderException(e.getMessage());
      }

      throw new UnprocessableEntityException(e.getMessage());
    }
  }

  @Override
  public ConfirmSignUpResult confirmSignUp(ConfirmSignUpRequest request) {
    try {
      return cognitoService.confirmSignUp(request.getEmail(), request.getConfirmationCode());

    } catch (Exception e) {
      if (e.toString().contains("AWSCognitoIdentityProvider")) {
        throw new AWSCognitoIdentityProviderException(e.getMessage());
      }

      throw new UnprocessableEntityException(e.getMessage());
    }
  }

  @Override
  public ResendConfirmationCodeResult resendConfirmationCode(ResendConfirmationCodeRequest request) {
    return cognitoService.resendConfirmationCode(request.getEmail());
  }

  @Override
  public UpdateUserAttributesResult updateUserAttributes(String accessToken, UpdateUserAttributesRequest request) {
    return cognitoService.updateUserAttributes(accessToken, request.getEmail(), request.getPhoneNumber(),
        request.getFullName());
  }

  @Override
  public ChangePasswordResult changePassword(String accessToken, ChangePasswordRequest request) {
    return cognitoService.changePassword(accessToken, request.getPreviousPassword(), request.getProposedPassword());
  }

  @Override
  public ForgotPasswordResult forgotPassword(ForgotPasswordRequest request) {
    return cognitoService.forgotPassword(request.getEmail());
  }

}
