package com.anonymasn.forum.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.anonymasn.forum.dao.RoleDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.model.ERole;
import com.anonymasn.forum.model.Role;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.LoginRequest;
import com.anonymasn.forum.payload.request.SignupRequest;
import com.anonymasn.forum.payload.response.JwtResponse;
import com.anonymasn.forum.security.jwt.JwtUtils;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDao userDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	EmailService emailService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

  @Override
  public User create(SignupRequest signUpRequest) {
    if (userDao.existsByUsername(signUpRequest.getUsername())) {
			return null;
		}

		if (userDao.existsByEmail(signUpRequest.getEmail())) {
			return null;
		}

		// Create new user's account
		User user = new User(
			signUpRequest.getFirstName(),
			signUpRequest.getLastName(),
			signUpRequest.getEmail(),
			signUpRequest.getPhone(),
			signUpRequest.getUsername(),
      encoder.encode(signUpRequest.getPassword())
    );
    user.setSex(signUpRequest.isSex() ? "F" : "H");
    user.setAge(signUpRequest.getAge());
    Set<Role> roles = convertToRoles(signUpRequest.getRoles());
    
    user.setRoles(roles);
		userDao.save(user);

    emailService.sendSimpleMessage(user.getEmail(),"Email d'invitation",
      String.format("Bonjour '%s' '%s' \nBienvenue sur Anonymous", user.getFirstName(), user.getLastName()));
    
    return user;
  }

  @Override
  public JwtResponse authenticate(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
			.map(item -> item.getAuthority())
      .collect(Collectors.toList());
    
    if(!userDetails.isAccountNonLocked()) {
      return null;
    }

		return new JwtResponse(jwt, 
      userDetails.getId(),
      userDetails.getFirstName(),
      userDetails.getLastName(),
      userDetails.getUsername(), 
      userDetails.getEmail(), 
      roles);
  }

  @Override
  public Page<User> findAll(int page, int size) {
    final Pageable pageable = PageRequest.of(page, size, Sort.by(Order.desc("id")));
    return userDao.findAll(pageable);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userDao.findByEmail(email);
  }

  public void updateStatus(String id, String action) {
    Optional<User> currUser = userDao.findById(id);
    if(!currUser.isPresent()) {
      return;
    }
    User user = currUser.get();
    switch (action) {
      case "active":
        user.setStatus(1);
        break;
      
      case "delete":
        user.setStatus(-1);
        break;
    
      default:
        break;
    }
    userDao.save(user);
  }

  @Override
  public boolean hasAuthorities(String role) {
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getAuthorities().contains(new SimpleGrantedAuthority(role));
  }

  @Override
  public Set<Role> convertToRoles(Set<String> strRoles) {
    Set<Role> roles = new HashSet<>();
    if((strRoles.contains("mod") && !hasAuthorities("ROLE_ADMIN")) || (strRoles.contains("admin") && !hasAuthorities("ROLE_ADMIN"))) {
      throw new RuntimeException("Access denied");
    }
    for (String role : strRoles) {
      switch (role) {
        case "admin":
          Role adminRole = roleDao.findByName(ERole.ROLE_ADMIN)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
  
          break;
        case "mod":
          Role modRole = roleDao.findByName(ERole.ROLE_MODERATOR)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);
  
          break;
        default:
          Role userRole = roleDao.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
      }
    }

    return roles;
  }
}