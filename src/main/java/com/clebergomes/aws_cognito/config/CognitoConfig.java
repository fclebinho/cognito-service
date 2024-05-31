package com.clebergomes.aws_cognito.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@Configuration
public class CognitoConfig {
  @Value("${aws.access-key-id}")
  private String accessKey;

  @Value("${aws.secret-key}")
  private String secretKey;

  @Value("${aws.region}")
  private String region;

  @Bean
  public AWSCognitoIdentityProvider cognitoIdentityProvider() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

    return AWSCognitoIdentityProviderClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(region)
        .build();
  }
}
