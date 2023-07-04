package com.training.library.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.services.AuthorService;
import com.training.library.services.GenreService;

@Controller
@RequestMapping("/api/v1/library/genres")
public class GenreController {

	@Autowired
	private GenreService genreService;
	@PostMapping("/excel")
	public ResponseEntity<ResponseDto> saveGenres(MultipartFile file) throws IOException, CustomExceptionHandler{
		
		ResponseDto result = this.genreService.saveGenres(file);
		
		return ResponseEntity.ok(result);
	}
}
