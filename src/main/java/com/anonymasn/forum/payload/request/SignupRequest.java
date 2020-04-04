package com.anonymasn.forum.payload.request;

import java.util.Set;

import javax.validation.constraints.*;
 
public class SignupRequest {
	@NotBlank
	@Size(min = 3, max = 150)
	private String firstName;

	@NotBlank
	@Size(min = 3, max = 100)
	private String lastName;

	@Size(max = 20)
  private String phone;

	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	private Set<String> roles;
	
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirsttName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

	public void setlastName(String lastName) {
		this.lastName = lastName;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<String> getRoles() {
		return this.roles;
	}
	
	public void setRole(Set<String> roles) {
		this.roles = roles;
	}
}
