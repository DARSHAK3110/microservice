package com.training.library.dto.response;

public class CustomBaseResponseDto {
	private String message;
	private Long saveEntityId;

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
