package com.training.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthorDto {
	@Size(min = 3, max = 16, message = "author.excel.validation.authorName")
	@NotNull(message = "author.excel.validation.null")
	@NotBlank(message = "author.excel.validation.blank")
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

	public AuthorDto(@Size(min = 3, max = 16, message = "author.excel.validation.authorName") String authorName) {
		super();
		this.authorName = authorName;
	}

}
