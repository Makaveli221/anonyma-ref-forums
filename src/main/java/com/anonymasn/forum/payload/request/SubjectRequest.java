package com.anonymasn.forum.payload.request;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SubjectRequest {

	@Size(min = 3, max = 150, message = "Title incorrect")
	@NotEmpty(message = "Please enter title")
	private String title;

	@NotEmpty(message = "Please enter description")
	private String description;

	@NotEmpty(message = "Please enter keywords")
	private Set<String> keywords;

	@NotEmpty(message = "Please enter typeSubject")
	private String typeSubject;

	@NotNull
	private int status;

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

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}