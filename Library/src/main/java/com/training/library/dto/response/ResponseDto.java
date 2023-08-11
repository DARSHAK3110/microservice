package com.training.library.dto.response;

import java.util.Map;

public class ResponseDto {
	String message;
	Map<String, String> result;
	 
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getResult() {
		return result;
	}

	public void setResult(Map<String, String> result) {
		this.result = result;
	}

	public ResponseDto(String message, Map<String, String> result) {
		super();
		this.message = message;
		this.result = result;
	}

	public ResponseDto() {
		super();
	}

}
