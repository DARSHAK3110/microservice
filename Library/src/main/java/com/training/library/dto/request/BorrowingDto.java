package com.training.library.dto.request;

import java.util.Date;

public class BorrowingDto {
	private Long bookId;
	private Long memberId;
	private Date borrowingDate;
	private Date returnDate;
	private Date dueDate;

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BorrowingDto() {
		super();
	}

	public BorrowingDto(Long bookId, Long memberId, Date borrowingDate, Date returnDate, Date dueDate) {
		super();
		this.bookId = bookId;
		this.memberId = memberId;
		this.borrowingDate = borrowingDate;
		this.returnDate = returnDate;
		this.dueDate = dueDate;
	}

}
