package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Set;

import com.anonymasn.forum.model.Role;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.LoginRequest;
import com.anonymasn.forum.payload.request.SignupRequest;
import com.anonymasn.forum.payload.response.JwtResponse;

import org.springframework.data.domain.Page;

public interface UserService {

  public User create(SignupRequest signUpRequest);

  public JwtResponse authenticate(LoginRequest loginRequest);

  public Page<User> findAll(int page, int size);

  public Optional<User> findByEmail(String email);

  public boolean hasAuthorities(String role); 

  public void updateStatus(String id, String action);

  public Set<Role> convertToRoles(Set<String> strRoles);
}