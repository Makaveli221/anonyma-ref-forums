package com.anonymasn.forum.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
public class Comment {
	@Id
	private String id;

	@DBRef
	@Field(value = "topic")
	private Topic topic;

	@DBRef
	@Field(value = "parent")
	private Comment parent;

	@NotBlank
	@Field(value = "message")
	private String message;

	@Field(value = "appreciations")
	private Set<Appreciation> appreciations = new HashSet<>();

	@JsonIgnore
	@DBRef
	@Field(value = "createUser")
	private User createUser;

	@Field(value = "createDate")
	private Date createDate;

	public Comment() {
	}

	public Comment(String message) {
		this.message = message;
		this.createDate = new Date();
	}

	public Comment(String message, User createUser) {
		this.message = message;
		this.createUser = createUser;
		this.createDate = new Date();
	}

	public Comment(String message, Topic topic, Set<Appreciation> appreciations, User createUser) {
		this.message = message;
		this.topic = topic;
		this.appreciations = appreciations;
		this.createUser = createUser;
		this.createDate = new Date();
	}

	public Comment(String message, Set<Appreciation> appreciations, Comment parent, User createUser) {
		this.message = message;
		this.parent = parent;
		this.appreciations = appreciations;
		this.createUser = createUser;
		this.createDate = new Date();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Topic getTopic() {
		return this.topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Comment getParent() {
		return this.parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<Appreciation> getAppreciations() {
		return this.appreciations;
	}

	public void setAppreciations(Set<Appreciation> appreciations) {
		this.appreciations = appreciations;
	}

	public User getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}