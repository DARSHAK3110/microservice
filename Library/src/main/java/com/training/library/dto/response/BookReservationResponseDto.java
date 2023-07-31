package com.training.library.dto.response;

import java.util.Date;

public class BookReservationResponseDto {
	private Long bookReservationId;
	private Long userId;
	private String bookTitle;
	private Long bookId;
	private Long phone;
	private String reserverName;
	private Date reservationDate;
	private Long isbn;

	public Long getBookReservationId() {
		return bookReservationId;
	}

	public void setBookReservationId(Long bookReservationId) {
		this.bookReservationId = bookReservationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public BookReservationResponseDto(Long bookReservationId, Long userId, String bookTitle, Long bookId, Long phone,
			Date reservationDate, Long isbn) {
		super();
		this.bookReservationId = bookReservationId;
		this.userId = userId;
		this.bookTitle = bookTitle;
		this.bookId = bookId;
		this.phone = phone;
		this.reservationDate = reservationDate;
		this.isbn = isbn;
	}

}
