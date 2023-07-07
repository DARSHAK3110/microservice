package com.training.library.dto.request;

import jakarta.validation.constraints.Size;

public class GenreDto {
	@Size(min = 3, max = 16, message = "genre.excel.validation.genreName")
	private String genreName;

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public GenreDto() {
		super();
	}
}
