package com.training.library.dto.request;

import jakarta.validation.constraints.Min;

public class ReservationStatusDto {
	@Min(value = 1, message = "bookReservation.validation.bookstatus.id")
	private Long bookStatusId;
	private Boolean status = false;

	public Long getBookStatusId() {
		return bookStatusId;
	}

	public void setBookStatusId(Long bookStatusId) {
		this.bookStatusId = bookStatusId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public ReservationStatusDto() {
		super();
	}

}
