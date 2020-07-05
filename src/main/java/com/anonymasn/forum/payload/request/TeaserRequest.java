package com.anonymasn.forum.payload.request;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TeaserRequest {

  @Size(min = 3, max = 150, message = "Title incorrect")
	@NotEmpty(message = "Please enter title")
	private String title;

	@NotEmpty(message = "Please enter description")
	private String description;

	@NotEmpty(message = "Please enter keywords")
	private Set<String> keywords;

	@NotEmpty(message = "Please enter type")
	private String typeSubject;

	@NotEmpty(message = "Please enter presentation")
	private String presentation;

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

	public String getTypeSubject() {
		return this.typeSubject;
	}

	public void setTypeSubject(String typeSubject) {
		this.typeSubject = typeSubject;
	}

	public String getPresentation() {
    return this.presentation;
  }

  public void setPresentation(String presentation) {
    this.presentation = presentation;
  }

}