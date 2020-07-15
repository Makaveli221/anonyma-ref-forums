package com.anonymasn.forum.payload.request;

import javax.validation.constraints.*;
 
public class SignupRequest extends UserRequest {

	private boolean sex;

	private int age;
	
	@NotEmpty(message = "Please enter password")
	@Size(min = 6, max = 40)
	private String password;

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
