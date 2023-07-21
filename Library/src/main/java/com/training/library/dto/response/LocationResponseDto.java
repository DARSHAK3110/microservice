package com.training.library.dto.response;

public class LocationResponseDto {
	private Long locationId;
	private String position;
	private ShelfResponseDto shelf;

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public ShelfResponseDto getShelf() {
		return shelf;
	}

	public void setShelf(ShelfResponseDto shelf) {
		this.shelf = shelf;
	}

	public LocationResponseDto() {
		super();
	}

	public LocationResponseDto(Long locationId, String position, ShelfResponseDto shelf) {
		super();
		this.locationId = locationId;
		this.position = position;
		this.shelf = shelf;
	}

}
