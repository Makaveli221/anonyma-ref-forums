package com.anonymasn.forum.payload.request;

import java.util.Set;

import javax.validation.constraints.*;
 
public class UserRequest {
	@NotEmpty(message = "Please enter firstName")
	@Size(min = 3, max = 150)
	private String firstName;

	@NotEmpty(message = "Please enter lastName")
	@Size(min = 3, max = 100)
	private String lastName;

	@NotEmpty(message = "Please enter email")
	@Size(max = 50)
	@Email
	private String email;

	@Size(max = 20)
  	private String phone;

	@NotEmpty(message = "Please enter username")
	@Size(min = 3, max = 20)
	private String username;

	@NotEmpty(message = "Please enter roles")
	private Set<String> roles;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
    return phone;
  }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<String> getRoles() {
		return this.roles;
	}
}
