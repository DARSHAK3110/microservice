package com.training.library.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.services.BorrowingService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/library/borrowings")
public class BorrowingController {

	@Autowired
	private BorrowingService borrowingService;
	@PostMapping("/excel")
	public ResponseEntity<ResponseDto> saveBorrowings(MultipartFile file, HttpServletRequest req) throws IOException, CustomExceptionHandler, URISyntaxException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		String header = req.getHeader("username");
		ResponseDto result = this.borrowingService.saveBorrowings(file,header);
		
		return ResponseEntity.ok(result);
	}
}
