package com.training.library.dto.request;

public class EmailRequestDto {
	private String email;
	private Boolean status;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public EmailRequestDto() {
		super();
	}

}
