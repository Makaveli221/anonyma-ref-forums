package com.anonymasn.forum.model;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "typesubjects")
public class Typesubject {
	@Id
	private String id;
	
	@NotBlank
	private String name;

	private boolean active;

	public Typesubject() {
	}
	
	public Typesubject(String name, boolean active) {
		this.name = name;
		this.active = active;
  }

	public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}