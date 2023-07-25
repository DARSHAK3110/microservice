package com.training.authentication.dto.response;

public class TokenResponseDto extends CustomBaseResponseDto {
	private String token;
	private String role;
	private Long userId;
	private String refreshToken;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public TokenResponseDto() {
		super();
	}

	public TokenResponseDto(String message) {
		super(message);
	}
}
