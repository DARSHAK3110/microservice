package com.training.library.dto.response;

public class FloorResponseDto {
	private Long floorNo;
	private Long floorId;

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

	public FloorResponseDto() {
		super();
	}

	public FloorResponseDto( Long floorId, Long floorNo) {
		super();
		this.floorNo = floorNo;
		this.floorId = floorId;
	}

}
