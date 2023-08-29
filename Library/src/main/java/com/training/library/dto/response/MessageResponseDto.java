package com.training.library.dto.response;

public class MessageResponseDto {
	private String entity;
	private Long entityId;
	private Object entityRequestDto;
	private String userName;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Object getEntityRequestDto() {
		return entityRequestDto;
	}

	public void setEntityRequestDto(Object entityRequestDto) {
		this.entityRequestDto = entityRequestDto;
	}

	public MessageResponseDto() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
