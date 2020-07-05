package com.anonymasn.forum.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class MessageRequest {
	@NotEmpty(message = "Please enter the type of Message")
  private String type;
  
	@NotEmpty(message = "Please enter the email id of Message")
	private String email;

	@NotEmpty(message = "Please enter texte")
	private String texte;

	private String response;

  private boolean validate;

  private boolean published;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
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
}