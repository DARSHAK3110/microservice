package com.training.authentication.dto.request;

public class UserLoginRequestDto {
	private Long phoneNumber;
	private String password;
	public Long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserLoginRequestDto() {
		super();
	}
	
	
}