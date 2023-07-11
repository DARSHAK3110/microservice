package com.training.library.dto.request;

import com.training.library.constraint.PhoneConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LibrariranDto {
	@Size(min = 3, max = 16, message = "librarian.excel.validation.memberName")
	private String name;
	@Email(message = "librarian.excel.validation.email")
	@NotBlank(message = "librarian.excel.validation.blank")
	private String email;
	@PhoneConstraint
	private Long phone;

	public LibrariranDto() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public LibrariranDto(@Size(min = 3, max = 16, message = "member.excel.validation.memberName") String name,
			@Email(message = "member.excel.validation.email") @NotBlank String email, Long phone) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

}
