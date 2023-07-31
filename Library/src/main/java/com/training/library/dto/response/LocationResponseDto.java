package com.training.library.dto.response;

public class LocationResponseDto {
	private Long locationId;
	private String position;
	private Long shelfId;
	private Long shelfNo;
	private Long sectionId;
	private String sectionName;
	private Long floorNo;
	private Long floorId;
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

	public Long getShelfId() {
		return shelfId;
	}

	public void setShelfId(Long shelfId) {
		this.shelfId = shelfId;
	}

	public Long getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(Long shelfNo) {
		this.shelfNo = shelfNo;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Long getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(Long floorNo) {
		this.floorNo = floorNo;
	}

	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public LocationResponseDto() {
		super();
	}

	public LocationResponseDto(Long locationId, String position, Long shelfId, Long shelfNo, Long sectionId,
			String sectionName, Long floorNo, Long floorId) {
		super();
		this.locationId = locationId;
		this.position = position;
		this.shelfId = shelfId;
		this.shelfNo = shelfNo;
		this.sectionId = sectionId;
		this.sectionName = sectionName;
		this.floorNo = floorNo;
		this.floorId = floorId;
		
	}

}
