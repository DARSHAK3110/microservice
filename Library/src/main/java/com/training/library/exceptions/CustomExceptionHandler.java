package com.training.library.exceptions;

public class CustomExceptionHandler extends Exception {

	private String file;

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
