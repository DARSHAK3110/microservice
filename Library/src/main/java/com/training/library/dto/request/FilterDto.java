package com.training.library.dto.request;

public class FilterDto {
	private String search = "";
	private int pageNumber;
	private int pageSize;
	private int availability;
	private boolean user = false;
	private String startDate;
	private String endDate;
	private boolean deletedAt;

	public FilterDto() {
		super();
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(boolean deletedAt) {
		this.deletedAt = deletedAt;
	}

}
