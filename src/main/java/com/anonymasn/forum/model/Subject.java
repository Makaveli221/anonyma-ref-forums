package com.anonymasn.forum.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "subjects")
public class Subject {
  @Id
	private String id;
	
	@NotBlank
	@Field(value = "title")
	private String title;

	@NotBlank
	@Field(value = "description")
	private String description;

	@NotBlank
	@Field(value = "keywords")
	private Set<String> keywords = new HashSet<>();

	@Field(value = "key")
	private String key;

	@NotBlank
	@DBRef
	@Field(value = "typeSubject")
	private Typesubject typeSubject;

	@NotBlank
	@DBRef
	@Field(value = "createUser")
	private User createUser;

	@Field(value = "status")
	private int status;

	@Field(value = "createDate")
	private Date createDate;

	public Subject() {
	}

	public Subject(String title, String description, Set<String> keywords, String key, Typesubject typeSubject, User createUser) {
		this.title = title;
		this.description = description;
		this.keywords = keywords;
		this.key = key;
		this.typeSubject = typeSubject;
		this.createUser = createUser;
		this.status = 0;
		this.createDate = new Date();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getKeywords() {
		return this.keywords;
	}

	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Typesubject getTypeSubject() {
		return this.typeSubject;
	}

	public void setTypeSubject(Typesubject typeSubject) {
		this.typeSubject = typeSubject;
	}

	public User getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}