package com.training.library.dto.response;

public class BookStatusResponseDto {

	private Long bookStatusId;
	private Long locationId;
	private Long floorId;
	private Long sectionId;
	private Long shelfId;
	private Long floorNo;
	private String sectionName;
	private Long shelfNo;
	private String position;
	private Boolean isAvailable;
	private Long isbn;
	private Long bookDetailsId;

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

	public Long getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(Long floorNo) {
		this.floorNo = floorNo;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Long getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(Long shelfNo) {
		this.shelfNo = shelfNo;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public Long getBookDetailsId() {
		return bookDetailsId;
	}

	public void setBookDetailsId(Long bookDetailsId) {
		this.bookDetailsId = bookDetailsId;
	}

	public BookStatusResponseDto(Long bookStatusId, Long locationId, Long floorId, Long sectionId, Long shelfId,
			Long floorNo, String sectionName, Long shelfNo, String position, Boolean isAvailable, Long isbn,
			Long bookDetailsId) {
		super();
		this.bookStatusId = bookStatusId;
		this.locationId = locationId;
		this.floorId = floorId;
		this.sectionId = sectionId;
		this.shelfId = shelfId;
		this.floorNo = floorNo;
		this.sectionName = sectionName;
		this.shelfNo = shelfNo;
		this.position = position;
		this.isAvailable = isAvailable;
		this.isbn = isbn;
		this.bookDetailsId = bookDetailsId;
	}

	public BookStatusResponseDto() {
		super();
	}

}
