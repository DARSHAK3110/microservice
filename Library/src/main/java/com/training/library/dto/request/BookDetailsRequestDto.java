package com.training.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class BookDetailsRequestDto {

	@Min(value= 1,message = "bookDetails.validation.bookDetails.id" )
	private Long bookDetailsId;
	
	private Long isbn;
	@NotBlank(message = "bookDetails.validation.title.blank")
	@Size(min = 3, message = "bookDetails.validation.title.size.min")
	@Size(max = 16, message = "bookDetails.validation.title.size.max")
	private String title;
	@Min(value= 1,message = "bookDetails.validation.author.id" )
	private Long authorId;
	
	private BookStatusRequestDto bookStatus;

	public Long getBookDetailsId() {
		return bookDetailsId;
	}

	public void setBookDetailsId(Long bookDetailsId) {
		this.bookDetailsId = bookDetailsId;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public BookStatusRequestDto getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(BookStatusRequestDto bookStatus) {
		this.bookStatus = bookStatus;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public BookDetailsRequestDto() {
		super();
	}

}
