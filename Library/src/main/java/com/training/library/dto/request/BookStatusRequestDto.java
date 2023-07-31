package com.training.library.dto.request;

import jakarta.validation.constraints.Min;

public class BookStatusRequestDto {
	@Min(value = 1, message = "bookStatus.validation.bookStatus.id")
	private Long bookStatusId;
	@Min(value = 1, message = "bookStatus.validation.location.id")
	private Long locationId;
	private Boolean isAvailable;

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Long getBookStatusId() {
		return bookStatusId;
	}

	public void setBookStatusId(Long bookStatusId) {
		this.bookStatusId = bookStatusId;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public BookStatusRequestDto() {
		super();
	}

}
