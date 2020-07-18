package com.anonymasn.forum.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.LoginRequest;
import com.anonymasn.forum.payload.request.SignupRequest;
import com.anonymasn.forum.payload.response.JwtResponse;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	UserService userService;


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		JwtResponse jwtRes = userService.authenticate(loginRequest);

		if(jwtRes == null)
			throw new RuntimeException("Compte désactivé ou supprimé.");

		return ResponseEntity.ok(jwtRes);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		User newUser = userService.signUp(signUpRequest);
		if (newUser == null) {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("Error: Email or Username is already taken!"));
		}
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
