package com.training.library.dto.request;

import java.util.Date;

import com.training.library.constraint.PhoneNumberConstraint;

import jakarta.validation.constraints.Min;

public class BookReservationRequestDto {
	@PhoneNumberConstraint
	private Long phone;
	@Min(value = 1, message = "bookReservation.validation.bookDetails.id")
	private Long bookDetailsId;
	private Date reservationDate;

	public BookReservationRequestDto() {
		super();
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public Long getBookDetailsId() {
		return bookDetailsId;
	}

	public void setBookDetailsId(Long bookDetailsId) {
		this.bookDetailsId = bookDetailsId;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}
}
