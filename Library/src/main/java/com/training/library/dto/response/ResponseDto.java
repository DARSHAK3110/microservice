package com.training.library.dto.response;

import java.util.Map;

public class ResponseDto {
	String message;
	Map<String, Object> result;
	 
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public ResponseDto(String message, Map<String, Object> result) {
		super();
		this.message = message;
		this.result = result;
	}

	public ResponseDto() {
		super();
	}

}
