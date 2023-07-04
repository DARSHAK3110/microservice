package com.training.library.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Borrowings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BORROWING_ID")
	private Long borrowingId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "BOOK_ID")
	private Books book;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID")
	private Members member;

	@Column(name = "BORROWING_DATE")
	private Date borrowingDate;

	@Column(name = "RETURN_DATE")
	private Date returnDate;

	@Column(name = "DUE_DATE")
	private Date dueDate;

	public Long getBorrowingId() {
		return borrowingId;
	}

	public void setBorrowingId(Long borrowingId) {
		this.borrowingId = borrowingId;
	}

	public Books getBook() {
		return book;
	}

	public void setBook(Books book) {
		this.book = book;
	}

	public Members getMember() {
		return member;
	}

	public void setMember(Members member) {
		this.member = member;
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

	public Borrowings() {
		super();
	}

}
