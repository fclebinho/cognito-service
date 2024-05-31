package com.clebergomes.aws_cognito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
import com.clebergomes.aws_cognito.requests.UserLoginRequest;
import com.clebergomes.aws_cognito.requests.UserRegistrationRequest;
import com.clebergomes.aws_cognito.responses.UserLoginResponse;
import com.clebergomes.aws_cognito.responses.UserRegistrationResponse;
import com.clebergomes.aws_cognito.service.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
public class UserController {

  @Autowired
  private UserServiceImpl userService;

  @PostMapping(value = "/register", consumes = { "application/json" })
  public ResponseEntity<UserRegistrationResponse> registerUser(
      @Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
    System.out.println(userRegistrationRequest);
    // Perform user registration
    UserRegistrationResponse registeredUser = userService.registerUser(userRegistrationRequest);
    if (registeredUser != null) {
      return ResponseEntity.ok(registeredUser);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    // Perform user login
    UserLoginResponse loggedInUser = userService.loginUser(userLoginRequest.getEmail(),
        userLoginRequest.getPassword());

    if (loggedInUser != null) {
      // Successful login
      return ResponseEntity.ok(loggedInUser);
    } else {
      // Invalid login, return an appropriate response
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/confirm-sign-up")
  public ResponseEntity<ConfirmSignUpResult> confirmSignUp(
      @Valid @RequestBody ConfirmSignUpRequest confirmSignUpRequest) {

    return ResponseEntity.ok(userService.confirmSignUp(confirmSignUpRequest));
  }

  @PostMapping("/resend-confirmation-code")
  public ResponseEntity<ResendConfirmationCodeResult> resendConfirmationCode(
      @Valid @RequestBody ResendConfirmationCodeRequest request) {
    return ResponseEntity.ok(userService.resendConfirmationCode(request));
  }

  @PostMapping("/update-user-attributes")
  public ResponseEntity<UpdateUserAttributesResult> updateUserAttributes(
      @Valid @RequestHeader(name = "Authorization") String token, @RequestBody UpdateUserAttributesRequest request) {
    return ResponseEntity.ok(userService.updateUserAttributes(token, request));
  }

  @PostMapping("/change-password")
  public ResponseEntity<ChangePasswordResult> changePassword(
      @Valid @RequestHeader(name = "Authorization") String token,
      @RequestBody ChangePasswordRequest request) {
    return ResponseEntity.ok(userService.changePassword(token, request));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<ForgotPasswordResult> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    return ResponseEntity.ok(userService.forgotPassword(request));
  }

}
