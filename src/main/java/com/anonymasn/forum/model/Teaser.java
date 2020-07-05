package com.anonymasn.forum.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "teasers")
public class Teaser {
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
  
  @NotBlank
	@DBRef
	@Field(value = "typeSubject")
  private Typesubject typeSubject;

  @Field(value = "presentation")
	private String presentation;
  
  @NotBlank
	@DBRef
	@Field(value = "createUser")
  private User createUser;
  
  @Field(value = "createDate")
  private Date createDate;
  
  public Teaser() {
	}

	public Teaser(String title, String description, Set<String> keywords, Typesubject typeSubject, String presentation,  User createUser) {
		this.title = title;
		this.description = description;
		this.keywords = keywords;
    this.typeSubject = typeSubject;
    this.presentation = presentation;
		this.createUser = createUser;
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

	public Typesubject getTypeSubject() {
		return this.typeSubject;
	}

	public void setTypeSubject(Typesubject typeSubject) {
		this.typeSubject = typeSubject;
  }
  
  public String getPresentation() {
    return this.presentation;
  }

  public void setPresentation(String presentation) {
    this.presentation = presentation;
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