package com.training.library.dto.request;

import jakarta.validation.constraints.Min;

public class FloorRequestDto {
	@Min(value= 1,message = "floor.validation.floor.id" )
	private Long floorId;
	@Min(value= 1,message = "floor.validation.floor.no" )
	private Long floorNo;

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

	public FloorRequestDto() {
		super();
	}

}
