package com.training.library.dto.request;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class BookDto {
	private Long isbn;
	@Size(min = 3, max = 16, message = "book.excel.validation.title")
	private String title;
	private Date publicationDate;
	@Min(value = 0, message = "book.excel.validation.availCopies")
	private Long availCopies;
	@Min(value = 0, message = "book.excel.validation.totalCopies")
	private Long totalCopies;
	private Long genreId;
	private Long authorId;

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

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Long getAvailCopies() {
		return availCopies;
	}

	public void setAvailCopies(Long availCopies) {
		this.availCopies = availCopies;
	}

	public Long getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(Long totalCopies) {
		this.totalCopies = totalCopies;
	}

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public BookDto(Long isbn, @Size(min = 3, max = 16, message = "book.excel.validation.title") String title,
			Date publicationDate, Long authorId) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.publicationDate = publicationDate;
		this.authorId = authorId;
	}



}
