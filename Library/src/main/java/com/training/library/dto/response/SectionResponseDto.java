package com.training.library.dto.response;

public class SectionResponseDto {
	private Long sectionId;
	private String sectionName;
	private Long floorId;
	private Long floorNo;

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

	public SectionResponseDto() {
		super();
	}

	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public Long getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(Long floorNo) {
		this.floorNo = floorNo;
	}

	public SectionResponseDto(Long sectionId, String sectionName, Long floorId, Long floorNo) {
		super();
		this.sectionId = sectionId;
		this.sectionName = sectionName;
		this.floorId = floorId;
		this.floorNo = floorNo;
	}

}
