package com.anonymasn.forum.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "messages")
public class Message {

  @Id
  private String id;
  
  @NotBlank
	@Field(value = "type")
  private EMessage type;

  @NotBlank
	@Field(value = "email")
  private String email;
  
  @NotBlank
	@Field(value = "texte")
  private String texte;

	@Field(value = "response")
  private String response;

  @Field(value = "validate")
  private boolean validate;

  @Field(value = "published")
  private boolean published;

  @Field(value = "appreciations")
  private Set<Appreciation> appreciations = new HashSet<>();
  
  @Field(value = "createDate")
  private Date createDate;

  @Field(value = "commentTotal")
  private long commentTotal;

  
  public Message() {
	}

	public Message(EMessage type, String email, String texte) {
    this.type = type;
    this.email = email;
    this.texte = texte;
    this.validate = false;
    this.published = false;
    this.createDate = new Date();
    this.commentTotal = 0;
  }
  
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EMessage getType() {
    return this.type;
  }

  public void setType(EMessage type) {
    this.type = type;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTexte() {
    return this.texte;
  }

  public void setTexte(String texte) {
    this.texte = texte;
  }

  public String getResponse() {
    return this.response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public boolean isValidate() {
    return this.validate;
  }

  public void setValidate(boolean validate) {
    this.validate = validate;
  }

  public boolean isPublished() {
    return this.published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }

  public Set<Appreciation> getAppreciations() {
		return this.appreciations;
	}

	public void setAppreciations(Set<Appreciation> appreciations) {
		this.appreciations = appreciations;
  }
  
  public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
  }
  
  public long getCommentTotal() {
		return this.commentTotal;
	}

	public void setCommentTotal(long commentTotal) {
		this.commentTotal = commentTotal;
	}
}