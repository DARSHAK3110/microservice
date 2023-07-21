package com.training.library.dto.response;

import java.util.List;

import com.training.library.entity.BookStatus;

public class BookDetailsResponseDto {

	private Long bookDetailsId;
	private String title;
	private Long isbn;
	private String author;
	private Long totalCopies;
	private Long availableCopies;
	private List<BookStatus> bookStatus;

	public Long getBookDetailsId() {
		return bookDetailsId;
	}

	public void setBookDetailsId(Long bookDetailsId) {
		this.bookDetailsId = bookDetailsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(Long totalCopies) {
		this.totalCopies = totalCopies;
	}

	public Long getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(Long availableCopies) {
		this.availableCopies = availableCopies;
	}

	public BookDetailsResponseDto() {
		super();
	}



	public List<BookStatus> getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(List<BookStatus> bookStatus) {
		this.bookStatus = bookStatus;
	}

	public BookDetailsResponseDto(Long bookDetailsId, String title, Long isbn, Long totalCopies, Long availableCopies,
			String author, List<BookStatus> bookStatus) {
		super();
		this.bookDetailsId = bookDetailsId;
		this.title = title;
		this.isbn = isbn;
		this.author = author;
		this.totalCopies = totalCopies;
		this.availableCopies = availableCopies;
		this.bookStatus = bookStatus;
	}

}
