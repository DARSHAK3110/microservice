package com.training.library.dto.response;

public class BookStatusResponseDto {

	private Long bookStatusId;
	private LocationResponseDto location;
	private Boolean isAvailable;

	public Long getBookStatusId() {
		return bookStatusId;
	}

	public void setBookStatusId(Long bookStatusId) {
		this.bookStatusId = bookStatusId;
	}

	public LocationResponseDto getLocation() {
		return location;
	}

	public void setLocation(LocationResponseDto location) {
		this.location = location;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public BookStatusResponseDto() {
		super();
	}

	public BookStatusResponseDto(Long bookStatusId, LocationResponseDto location, Boolean isAvailable) {
		super();
		this.bookStatusId = bookStatusId;
		this.location = location;
		this.isAvailable = isAvailable;
	}

}
