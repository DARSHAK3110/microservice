package com.training.library.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.services.BookDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/bookdetails")
public class BookDetailsController {
	@Autowired
	private BookDetailsService bookDetailsService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/book/{id}")
	public ResponseEntity<BookDetailsResponseDto> findBookDetails(@PathVariable Long id) {
		BookDetailsResponseDto bookDetails = bookDetailsService.findBookDetails(id);
		return ResponseEntity.ok(bookDetails);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping
	public ResponseEntity<Page<BookDetailsResponseDto>> findBookDetailss(FilterDto dto, HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Page<BookDetailsResponseDto> bookDetailsPage = bookDetailsService.findAllBookDetails(dto,userName);
		return ResponseEntity.ok(bookDetailsPage);
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveBookDetails(@Valid @RequestBody BookDetailsRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookDetailsService.saveBookDetails(dto, userName);
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/book/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateBookDetails(@Valid @PathVariable Long id,
			@RequestBody BookDetailsRequestDto dto, HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookDetailsService.updateBookDetails(id, dto, userName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/book/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBookDetails(@PathVariable Long id) {
		return bookDetailsService.deleteBookDetails(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/excel/{isbn}")
	public ResponseEntity<CustomBaseResponseDto> saveBooks(@PathVariable (name="isbn") Long isbn,MultipartFile file,HttpServletRequest req) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, CustomExceptionHandler, IOException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return this.bookDetailsService.uploadBooks(isbn,file,userName);
	}

}
