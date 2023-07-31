package com.training.library.dto.response;

import java.util.Date;

public class BookBorrowingResponseDto {

	private Long bookBorrowingId;
	private Long userId;
	private Long location;
	private Long bookId;
	private Long phone;
	private Date borrowingDate;
	private Date returnDate;
	private String bookTitle;

	public Long getBookBorrowingId() {
		return bookBorrowingId;
	}

	public void setBookBorrowingId(Long bookBorrowingId) {
		this.bookBorrowingId = bookBorrowingId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLocation() {
		return location;
	}

	public void setLocation(Long location) {
		this.location = location;
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

	public Date getBorrowingDate() {
		return borrowingDate;
	}

	public void setBorrowingDate(Date borrowingDate) {
		this.borrowingDate = borrowingDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public BookBorrowingResponseDto() {
		super();
	}

	public BookBorrowingResponseDto(Long bookBorrowingId, Long userId, Long location, Long bookId, Long phone,
			Date borrowingDate, Date returnDate, String bookTitle) {
		super();
		this.bookBorrowingId = bookBorrowingId;
		this.userId = userId;
		this.location = location;
		this.bookId = bookId;
		this.phone = phone;
		this.borrowingDate = borrowingDate;
		this.returnDate = returnDate;
		this.bookTitle = bookTitle;
	}

}
