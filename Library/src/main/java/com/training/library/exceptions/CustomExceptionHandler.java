package com.training.library.exceptions;

public class CustomExceptionHandler extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMesssage;
	private Long line;
	private String file;
	private Exception exception;
	private String fieldName;
	
	
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getErrorMesssage() {
		return errorMesssage;
	}

	public void setErrorMesssage(String errorMesssage) {
		this.errorMesssage = errorMesssage;
	}

	public Long getLine() {
		return line;
	}

	public void setLine(Long line) {
		this.line = line;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	public CustomExceptionHandler(String message) {
		super(message);
	}
	
	public CustomExceptionHandler(String file, String message) {
		super(message);
		this.file = file;
	}

	public CustomExceptionHandler() {
		super("Please use excel to store data!!");
	}

}
