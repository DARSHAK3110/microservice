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

@Controller
@RequestMapping("/api/v1/library/authors")
public class AuthorController {

	@Autowired
	private AuthorService autherService;
	@PostMapping("/excel")
	public ResponseEntity<ResponseDto> saveAuthors(MultipartFile file) throws IOException, CustomExceptionHandler{
		
		ResponseDto result = this.autherService.saveAuthors(file);
		
		return ResponseEntity.ok(result);
	}
}
