package com.clebergomes.aws_cognito.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.clebergomes.aws_cognito.model.User;
import com.clebergomes.aws_cognito.repository.UserRepository;
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

  @Autowired
  private UserRepository userRepository;

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
      SignUpResult signUpResponse = cognitoIdentityProvider.signUp(signUpRequest);

      Boolean isUserConfirmed = signUpResponse.isUserConfirmed();

      User registeredUser = new User();
      registeredUser.setPhoneNumber(input.getPhoneNumber());
      registeredUser.setEmail(input.getEmail());
      registeredUser.setFullName(input.getFullName());

      User newUser = userRepository.save(registeredUser);

      return new UserRegistrationResponse(
          newUser.getId(),
          newUser.getFullName(),
          newUser.getEmail(),
          newUser.getPhoneNumber(),
          isUserConfirmed);

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

    try {
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

    } catch (Exception e) {
      throw new RuntimeException("User login failed: " + e.getMessage(), e);
    }
  }

  void test() {
    cognitoIdentityProvider.confirmSignUp(null);
    cognitoIdentityProvider.resendConfirmationCode(null);
    cognitoIdentityProvider.updateUserAttributes(null);
    cognitoIdentityProvider.changePassword(null);
    cognitoIdentityProvider.forgotPassword(null);
  }
}

// handleSignUp (signUp)
// handleSignIn (signIn, resendSignUpCode | se estiver em CONFIRM_SIGN_UP)
// handleSendEmailVerificationCode (resendSignUpCode)
// handleConfirmSignUp (confirmSignUp)
// handleSignOut (signOut)
// | handleUpdateUserAttribute (updateUserAttribute)
// | handleUpdateUserAttributeNextSteps
// handleUpdatePassword (updatePassword)
// handleConfirmUserAttribute (confirmUserAttribute)
// handleResetPassword
// handleConfirmResetPassword