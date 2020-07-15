package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Collections;
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
import com.anonymasn.forum.payload.request.UserRequest;
import com.anonymasn.forum.payload.response.JwtResponse;
import com.anonymasn.forum.security.jwt.JwtUtils;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.apache.commons.lang3.RandomStringUtils;
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
  public User create(UserRequest userRequest) {
		if (!userRequest.getUsername().equals(userRequest.getEmail()) || userDao.existsByEmail(userRequest.getEmail())) {
			return null;
    }
    
    String password = generateCommonLangPassword();

		// Create new user's account
		User user = new User(
			userRequest.getFirstName(),
			userRequest.getLastName(),
			userRequest.getEmail(),
			userRequest.getPhone(),
			userRequest.getUsername(),
      encoder.encode(password)
    );
    Set<Role> roles = convertToRoles(userRequest.getRoles());
    
    user.setRoles(roles);
		userDao.save(user);

    emailService.sendSimpleMessage(user.getEmail(),"Bienvenue sur Anonym@",
      String.format("Bonjour %s %s. Vous venez d'être ajouter sur Anonym@. Votre mot de passe est %s. Une fois connecté veuillez changer votre mot de passe.",
      user.getFirstName(), user.getLastName(), password));
    
    return user;
  }

  @Override
  public User update(String id, UserRequest userRequest) {
    Optional<User> currUser = userDao.findById(id);

    if (!currUser.isPresent()) {
			return null;
    }
    
    Set<Role> roles = convertToRoles(userRequest.getRoles());

    User user = currUser.get();
    user.setFirstName(userRequest.getFirstName());
    user.setlastName(userRequest.getLastName());
    user.setEmail(userRequest.getEmail());
    user.setUsername(userRequest.getUsername());
    user.setPhone(userRequest.getPhone());
    user.setRoles(roles);
    userDao.save(user);
    return user;
  }

  @Override
  public void delete(String id) {
    userDao.deleteById(id);
  }

  @Override
  public User signUp(SignupRequest signUpRequest) {
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
  public Optional<User> findById(String id) {
    return userDao.findById(id);
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

  @Override
  public Collection<Role> getListRoles() {
    return roleDao.findAll();
  }

  public String generateCommonLangPassword() {
    String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
    String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
    String numbers = RandomStringUtils.randomNumeric(2);
    String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
    String totalChars = RandomStringUtils.randomAlphanumeric(2);
    String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
      .concat(numbers)
      .concat(specialChar)
      .concat(totalChars);
    List<Character> pwdChars = combinedChars.chars()
      .mapToObj(c -> (char) c)
      .collect(Collectors.toList());
    Collections.shuffle(pwdChars);
    String password = pwdChars.stream()
      .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
      .toString();
    return password;
  }
}