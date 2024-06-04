package com.clebergomes.aws_cognito.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.clebergomes.aws_cognito.requests.UserRegistrationRequest;
import com.clebergomes.aws_cognito.responses.UserLoginResponse;
import com.clebergomes.aws_cognito.responses.UserRegistrationResponse;

@Service
public class CognitoService {
  @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.cognito.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.registration.cognito.scope}")
  private String scope;

  @Value("${spring.security.oauth2.client.provider.cognito.issuer-uri}")
  private String issuerUri;

  @Autowired
  private AWSCognitoIdentityProvider cognitoIdentityProvider;

  public UserRegistrationResponse registerUser(UserRegistrationRequest input) {
    // Set up the AWS Cognito registration request
    SignUpRequest signUpRequest = new SignUpRequest()
        .withClientId(clientId)
        .withUsername(input.getEmail())
        .withPassword(input.getPassword())
        .withUserAttributes(
            new AttributeType().withName("email").withValue(input.getEmail()),
            new AttributeType().withName("phone_number").withValue(input.getPhoneNumber()),
            new AttributeType().withName("name").withValue(input.getFullName()));

    // Register the user with Amazon Cognito
    try {
      SignUpResult signUpResult = cognitoIdentityProvider.signUp(signUpRequest);

      Boolean isVerified = signUpResult.getUserConfirmed();

      return UserRegistrationResponse.builder()
          .fullName(input.getFullName())
          .email(input.getEmail())
          .phoneNumber(input.getPhoneNumber())
          .isVerified(isVerified)
          .build();

    } catch (Exception e) {
      throw new RuntimeException("User registration failed: " + e.getMessage(), e);
    }
  }

  public UserLoginResponse loginUser(String email, String password) {
    // Set up the authentication request
    InitiateAuthRequest authRequest = new InitiateAuthRequest()
        .withAuthFlow("USER_PASSWORD_AUTH")
        .withClientId(clientId)
        .withAuthParameters(
            Map.of(
                "USERNAME", email, // Use email as the username
                "PASSWORD", password));

    InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);

    AuthenticationResultType authResponse = authResult.getAuthenticationResult();

    // At this point, the user is successfully authenticated, and you can access JWT
    // tokens:
    String accessToken = authResponse.getAccessToken();
    String idToken = authResponse.getIdToken();
    String refreshToken = authResponse.getRefreshToken();
    Integer expiresIn = authResponse.getExpiresIn();
    String tokenType = authResponse.getTokenType();

    // You can decode and verify the JWT tokens for user information

    return new UserLoginResponse(accessToken, expiresIn, tokenType, refreshToken, idToken);
  }

  public ConfirmSignUpResult confirmSignUp(String email, String confirmationCode) {
    ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest()
        .withClientId(clientId)
        .withUsername(email)
        .withConfirmationCode(confirmationCode);

    return cognitoIdentityProvider.confirmSignUp(confirmSignUpRequest);
  }

  public ResendConfirmationCodeResult resendConfirmationCode(String email) {
    ResendConfirmationCodeRequest resendConfirmationCodeRequest = new ResendConfirmationCodeRequest()
        .withClientId(clientId)
        .withUsername(email);

    return cognitoIdentityProvider.resendConfirmationCode(resendConfirmationCodeRequest);
  }

  public UpdateUserAttributesResult updateUserAttributes(String accessToken, String email, String phoneNumber,
      String fullName) {
    UpdateUserAttributesRequest updateUserAttributesRequest = new UpdateUserAttributesRequest()
        .withAccessToken(accessToken)
        .withUserAttributes(
            new AttributeType().withName("email").withValue(email),
            new AttributeType().withName("phone_number").withValue(phoneNumber),
            new AttributeType().withName("name").withValue(fullName));

    return cognitoIdentityProvider.updateUserAttributes(updateUserAttributesRequest);
  }

  public ChangePasswordResult changePassword(String accessToken, String previousPassword, String proposedPassword) {
    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest()
        .withAccessToken(accessToken)
        .withPreviousPassword(previousPassword)
        .withProposedPassword(proposedPassword);

    return cognitoIdentityProvider.changePassword(changePasswordRequest);
  }

  public ForgotPasswordResult forgotPassword(String email) {
    ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
        .withClientId(clientId)
        .withUsername(email);

    return cognitoIdentityProvider.forgotPassword(forgotPasswordRequest);
  }

}