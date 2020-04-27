package com.anonymasn.forum.payload.request;

import java.util.Set;

import javax.validation.constraints.*;
 
public class SignupRequest {
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

	private boolean sex;

	private int age;
	
	@NotEmpty(message = "Please enter password")
	@Size(min = 6, max = 40)
	private String password;

	@NotEmpty(message = "Please enter roles")
	private Set<String> roles;

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

	public boolean isSex() {
		return this.sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
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
