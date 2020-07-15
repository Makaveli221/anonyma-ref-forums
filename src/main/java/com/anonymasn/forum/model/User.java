package com.anonymasn.forum.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {
  @Id
  private String id;

  @NotBlank
  @Field(value = "firstName")
  private String firstName;

  @NotBlank
  @Field(value = "lastName")
  private String lastName;

  @NotBlank
  @Email
  @Field(value = "email")
  private String email;

  @Field(value = "phone")
  private String phone;

  @NotBlank
  @Field(value = "username")
  private String username;

  @NotBlank
  @JsonIgnore
  @Field(value = "password")
  private String password;

  @NotBlank
  @Field(value = "createDate")
  private Date createDate;

  @JsonIgnore
  @Field(value = "status")
  private int status;

  @Field(value = "sex")
  private String sex;

  @Field(value = "age")
  private int age;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  public User() {
  }

  public User(String firstName, String lastName, String email, String phone, String username, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.username = username;
    this.password = password;
    this.createDate = new Date();
    this.status = 0;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String lastName) {
    this.lastName = lastName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setlastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getSex() {
    return this.sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return String.format("User[id=%s, firstName='%s', lastName='%s', email='%b']", id, firstName, lastName, email);
  }
}