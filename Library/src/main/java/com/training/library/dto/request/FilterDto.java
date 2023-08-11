package com.training.library.dto.request;

public class FilterDto {
	private String search = "";
	private int pageNumber;
	private int pageSize;
	private int availability;
	private boolean user = false;

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

	public FilterDto(String search, int pageNumber, int pageSize, int availability, boolean user) {
		super();
		this.search = search;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.availability = availability;
		this.user = user;
	}

}
