package com.anonymasn.forum.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anonymasn.forum.model.ERole;
import com.anonymasn.forum.model.Role;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.LoginRequest;
import com.anonymasn.forum.payload.request.SignupRequest;
import com.anonymasn.forum.payload.response.JwtResponse;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.dao.RoleDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.security.jwt.JwtUtils;
import com.anonymasn.forum.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDao userDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
			.map(item -> item.getAuthority())
			.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
			userDetails.getId(),
			userDetails.getFirstName(),
			userDetails.getLastName(),
			userDetails.getUsername(), 
			userDetails.getEmail(), 
			roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userDao.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userDao.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(
			signUpRequest.getFirstName(),
			signUpRequest.getLastName(),
			signUpRequest.getEmail(),
			signUpRequest.getPhone(),
			signUpRequest.getUsername(),
			encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleDao.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			boolean authorized = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					if (!authentication.isAuthenticated() || !authorized) {
						throw new RuntimeException("Access denied to create a user with Role Admin.");
					}
					Role adminRole = roleDao.findByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					if (!authentication.isAuthenticated() || !authorized) {
						throw new RuntimeException("Access denied to create a user with Role Moderator.");
					}
					Role modRole = roleDao.findByName(ERole.ROLE_MODERATOR)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleDao.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userDao.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
