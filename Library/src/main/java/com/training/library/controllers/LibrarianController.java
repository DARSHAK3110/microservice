package com.training.library.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.response.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.services.LibrarianService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/library/librarians")
public class LibrarianController {

	@Autowired
	private LibrarianService librarianService;
	@PostMapping("/excel")
	public ResponseEntity<ResponseDto> saveLbrarians(MultipartFile file, HttpServletRequest req) throws IOException, CustomExceptionHandler, URISyntaxException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		String header = req.getHeader("username");
		ResponseDto result = this.librarianService.saveLibrarians(file,header);
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/upload/{uploadId}")
	public ResponseEntity<ResponseDto> getLibrsariansByUserId(@PathVariable Long uploadId, @RequestParam int pageNumber,@RequestParam int pageSize){
		ResponseDto result = this.librarianService.getLibrariansByUploadId(uploadId, pageNumber, pageSize);
		return ResponseEntity.ok(result);
	}
}
