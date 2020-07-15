package com.anonymasn.forum.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.anonymasn.forum.model.Role;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.UserRequest;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

  @Autowired
  UserService userService;
  
  @GetMapping("/all")
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
	
	@PostMapping("/add")
	public ResponseEntity<?> add(@Valid @RequestBody UserRequest userRequest) {
		User newUser = userService.create(userRequest);
		if (newUser == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: Email or Username is already taken!"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@PathVariable(value = "id") String id, @Valid @RequestBody UserRequest userRequest) {
		User updateUser = userService.update(id, userRequest);
		if (updateUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: User not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(updateUser);
	}
  
  @DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
		Optional<User> currUser = userService.findById(id);
		if (!currUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Teaser not found or already deleted."));
		}
		userService.delete(currUser.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User is successfully deleted"));
	}

	@GetMapping("/roles")
	public ResponseEntity<?> listRoles() {
		Collection<Role> listRoles = userService.getListRoles();
		return ResponseEntity.status(HttpStatus.OK).body(listRoles);
	}
}