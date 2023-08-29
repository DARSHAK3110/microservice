package com.training.authentication.dto.response;

public class CustomBaseResponseDto {
	protected String message;
	private Long saveEntityId;

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

	public Long getSaveEntityId() {
		return saveEntityId;
	}

	public void setSaveEntityId(Long saveEntityId) {
		this.saveEntityId = saveEntityId;
	}

	public CustomBaseResponseDto(String message, Long saveEntityId) {
		super();
		this.message = message;
		this.saveEntityId = saveEntityId;
	}

}
