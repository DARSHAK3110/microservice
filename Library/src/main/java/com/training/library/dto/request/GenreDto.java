package com.training.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GenreDto {
	@Size(min = 3, max = 16, message = "genre.excel.validation.genreName")
	@NotNull(message = "genre.excel.validation.null")
	@NotBlank(message = "genre.excel.validation.blank")
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

	public GenreDto(@Size(min = 3, max = 16, message = "genre.excel.validation.genreName") String genreName) {
		super();
		this.genreName = genreName;
	}
	
}
