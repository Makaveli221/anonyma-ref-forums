package com.anonymasn.forum.payload.request;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
 
public class UpdateUserRequest {
	
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

	@NotEmpty(message = "Please enter username")
	@Size(min = 3, max = 50)
	private String username;

	@Size(max = 20)
	private String phone;
	
	private boolean sex;

	private int age;
	
	private String password;


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

	public boolean isSex() {
		return this.sex;
	}

	public int getAge() {
		return this.age;
	}

	public String getPassword() {
		return password;
	}
}
