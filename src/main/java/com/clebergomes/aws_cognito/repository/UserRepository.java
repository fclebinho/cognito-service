package com.clebergomes.aws_cognito.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clebergomes.aws_cognito.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
