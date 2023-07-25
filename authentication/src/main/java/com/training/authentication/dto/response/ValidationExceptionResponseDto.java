package com.training.authentication.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ValidationExceptionResponseDto extends ExceptionResponseDto {
	private String fieldName;

	@Override
	@JsonIgnore
	public Boolean getIsError() {
		return super.getIsError();
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
