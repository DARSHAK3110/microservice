package com.training.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SectionRequestDto {
	@NotBlank(message = "section.validation.name.blank")
	@Size(max = 16, message = "section.validation.name.size.max")
	private String sectionName;
	@Min(value = 1, message = "section.validation.floor.id")
	private Long floorNo;
	@Min(value = 0, message = "section.validation.section.id")
	private Long sectionId;

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

	public SectionRequestDto() {
		super();
	}

}
