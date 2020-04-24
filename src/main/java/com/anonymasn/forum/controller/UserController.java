package com.anonymasn.forum.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.anonymasn.forum.model.User;
import com.anonymasn.forum.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService userService;
  
  @GetMapping("/")
	public ResponseEntity<?> All(@RequestParam Map<String, String> customQuery) {
		int page = 0;
		int limit = 10;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			limit = Integer.parseInt(customQuery.get("limit"));
		}
		Page<User> users = userService.findAll(page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(users);
  }
  
  @GetMapping("/{email}")
	public ResponseEntity<?> Single(@PathVariable(value = "email") String email) {
		Optional<User> user = userService.findByEmail(email);
		return ResponseEntity.status(HttpStatus.OK).body(user);
  }
}