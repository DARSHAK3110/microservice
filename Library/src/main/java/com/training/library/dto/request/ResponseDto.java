package com.training.library.dto.request;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseDto {
	String message;
	Map<String, Object> result;
	String fileUrl;
	 

	
	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

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
