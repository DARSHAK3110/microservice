package com.training.authentication.dto.response;

import java.util.Date;

public class ClaimsResponseDto extends CustomBaseResponseDto {
	private String role;
	private String name;
	private String subject;
	private Date issuedDate;
	private Date expireDate;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public ClaimsResponseDto() {
		super();
	}

	public ClaimsResponseDto(String message, String role, String name, String subject, Date issuedDate,
			Date expireDate) {
		super(message);
		this.role = role;
		this.name = name;
		this.subject = subject;
		this.issuedDate = issuedDate;
		this.expireDate = expireDate;
	}

	public ClaimsResponseDto(String message) {
		super(message);
	}

}
