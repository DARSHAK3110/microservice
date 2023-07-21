package com.training.library.dto.response;

import java.util.Date;

public class AuthorResponseDto {

	private Long authorId;
	private String authorName;
	private Date authorDOB;

	public AuthorResponseDto() {
		super();
	}

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

	public AuthorResponseDto(Long authorId, String authorName, Date authorDOB) {
		super();
		this.authorId = authorId;
		this.authorName = authorName;
		this.authorDOB = authorDOB;
	}

	public AuthorResponseDto(String authorName, Date authorDOB) {
		super();
		this.authorName = authorName;
		this.authorDOB = authorDOB;
	}
	
	

}
