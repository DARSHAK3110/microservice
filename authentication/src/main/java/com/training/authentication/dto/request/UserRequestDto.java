package com.training.authentication.dto.request;

import com.training.authentication.constraint.PhoneNumberConstraint;
import com.training.authentication.constraint.RoleConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

	@Min(value = 0, message = "validation.userId")
	private Long userId;
	@Size(max = 16, message = "validation.firstNameSize")
	@Pattern(regexp = "^(\\d|\\w)+$", message = "validation.firstNamePattern")
	private String firstName;
	@Size(max = 16, message = "validation.lastNameSize")
	@Pattern(regexp = "^(\\d|\\w)+$", message = "validation.lastNamePattern")
	private String lastName;

	@PhoneNumberConstraint
	private Long phoneNumber;

	@NotBlank(message = "validation.RoleValue")
	@RoleConstraint
	private String role;

	@Size(min = 6, max = 8, message = "validation.passwordSize")
	private String password;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRequestDto() {
		super();
	}

}
