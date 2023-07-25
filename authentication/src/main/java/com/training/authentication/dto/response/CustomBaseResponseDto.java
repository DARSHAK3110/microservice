package com.training.authentication.dto.response;

public class CustomBaseResponseDto {
	protected String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CustomBaseResponseDto(String message) {
		super();
		this.message = message;
	}

	public CustomBaseResponseDto() {
		super();
	}

}
