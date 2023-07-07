package com.training.library.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.services.AuthorService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/library/authors")
public class AuthorController {

	@Autowired
	private AuthorService autherService;
	@PostMapping("/excel")
	public ResponseEntity<ResponseDto> saveAuthors(MultipartFile file, HttpServletRequest req) throws IOException, CustomExceptionHandler, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		System.out.println(file.getContentType());
		System.out.println(file.getOriginalFilename());
		String header = req.getHeader("username");
		ResponseDto result = this.autherService.saveAuthors(file,header);
		return ResponseEntity.ok(result);
	}
}
