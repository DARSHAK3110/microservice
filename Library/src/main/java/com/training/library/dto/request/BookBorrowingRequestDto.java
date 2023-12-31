package com.training.library.dto.request;

import java.util.Date;

import com.training.library.constraint.DateValidator;
import com.training.library.constraint.PhoneNumberConstraint;

import jakarta.validation.constraints.Min;

public class BookBorrowingRequestDto {
	@Min(value = 1, message = "bookBorrowing.validation.bookBorrowing.id")
	private Long bookBorrowingId;
	@PhoneNumberConstraint
	private Long phone;
	@Min(value = 1, message = "bookBorrowing.validation.bookStatus.id")
	private Long bookStatusId;
	@DateValidator
	private Date borrowingDate;

	public Long getPhone() {
		return phone;
	}

	public Long getBookBorrowingId() {
		return bookBorrowingId;
	}

	public void setBookBorrowingId(Long bookBorrowingId) {
		this.bookBorrowingId = bookBorrowingId;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public Long getBookStatusId() {
		return bookStatusId;
	}

	public void setBookStatusId(Long bookStatusId) {
		this.bookStatusId = bookStatusId;
	}

	public Date getBorrowingDate() {
		return borrowingDate;
	}

	public void setBorrowingDate(Date borrowingDate) {
		this.borrowingDate = borrowingDate;
	}

	public BookBorrowingRequestDto() {
		super();
	}

}
