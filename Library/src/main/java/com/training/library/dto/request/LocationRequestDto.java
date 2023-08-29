package com.training.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LocationRequestDto {
	@NotBlank(message = "location.validation.position.blank")
	@Size(max = 16, message = "location.validation.position.size.max")
	private String position;
	@Min(value = 1, message = "location.validation.floor.id")
	private Long floorId;
	@Min(value = 1, message = "location.validation.section.id")
	private Long sectionId;
	@Min(value = 1, message = "location.validation.shelf.id")
	private Long shelfId;
	private Long locationId;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Long getShelfId() {
		return shelfId;
	}

	public void setShelfId(Long shelfId) {
		this.shelfId = shelfId;
	}

	public LocationRequestDto() {
		super();
	}

}
