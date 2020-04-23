package com.anonymasn.forum.payload.request;

import javax.validation.constraints.NotEmpty;

public class CommentRequest {
	@NotEmpty(message = "Please enter the source of comment")
  private String source;
  
	@NotEmpty(message = "Please enter the source id of comment")
	private String idSource;

	@NotEmpty(message = "Please enter message")
	private String message;


	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIdSource() {
		return this.idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}