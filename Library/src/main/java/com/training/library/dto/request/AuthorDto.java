package com.training.library.dto.request;

import jakarta.validation.constraints.Size;

public class AuthorDto {
	@Size(min=3, max=16, message="author.excel.validation.authorName")
	private String authorName;


	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public AuthorDto() {
		super();
	}

}
