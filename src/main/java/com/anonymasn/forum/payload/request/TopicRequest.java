package com.anonymasn.forum.payload.request;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class TopicRequest {

  @Size(min = 3, max = 150, message = "Title incorrect")
	@NotEmpty(message = "Please enter title")
	private String title;

	@NotEmpty(message = "Please enter description")
	private String description;

	@NotEmpty(message = "Please enter keywords")
	private Set<String> keywords;

	private MultipartFile imgDefault;

	private String data;

	@NotEmpty(message = "Please enter Subject")
	private String subject;

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

	public MultipartFile getImgDefault() {
		return this.imgDefault;
	}

	public void setImgDefault(MultipartFile imgDefault) {
		this.imgDefault = imgDefault;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}