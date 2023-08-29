package com.training.library.dto.request;

import jakarta.validation.constraints.Min;

public class ShelfRequestDto {
	@Min(value= 0,message = "shelf.validation.shelf.id" )
	private Long shelfId;

	@Min(value= 0,message = "shelf.validation.shelf.no")
	private Long shelfNo;

	@Min(value= 0,message = "shelf.validation.section.id" )
	private Long sectionId;

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

	public ShelfRequestDto() {
		super();
	}
}
