package com.training.library.dto.request;

import java.util.Date;

import com.training.library.constraint.DateValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorRequestDto {
	private Long authorId;
	@NotBlank(message = "author.validation.name.blank")
	@Size(min = 3, message = "author.validation.name.size.min")
	@Size(max = 16, message = "author.validation.name.size.max")
	private String authorName;
	private Date authorDOB;

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Date getAuthorDOB() {
		return authorDOB;
	}

	public void setAuthorDOB(Date authorDOB) {
		this.authorDOB = authorDOB;
	}

	public AuthorRequestDto() {
		super();
	}

}
