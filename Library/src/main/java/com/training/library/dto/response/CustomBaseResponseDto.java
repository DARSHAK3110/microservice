package com.training.library.dto.response;

public class CustomBaseResponseDto {
	private String message;

	public CustomBaseResponseDto() {
		super();
	}

	public CustomBaseResponseDto(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
