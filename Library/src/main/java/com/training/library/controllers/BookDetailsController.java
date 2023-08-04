package com.training.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.BookDetailsService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/bookdetails")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BookDetailsController {
	@Autowired
	private BookDetailsService bookDetailsService;


	@GetMapping("/book/{id}")
	public ResponseEntity<BookDetailsResponseDto> findBookDetails(@PathVariable Long id) {
		BookDetailsResponseDto bookDetails = bookDetailsService.findBookDetails(id);
		return ResponseEntity.ok(bookDetails);
	}

	@GetMapping
	public ResponseEntity<Page<BookDetailsResponseDto>> findBookDetailss(FilterDto dto) {
		Page<BookDetailsResponseDto> bookDetailsPage = bookDetailsService.findAllBookDetails(dto);
		return ResponseEntity.ok(bookDetailsPage);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveBookDetails(@RequestBody BookDetailsRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookDetailsService.saveBookDetails(dto, userName);
	}

	@PutMapping("/book/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateBookDetails(@PathVariable Long id,
			@RequestBody BookDetailsRequestDto dto, HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookDetailsService.updateBookDetails(id, dto, userName);
	}

	@DeleteMapping("/book/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBookDetails(@PathVariable Long id) {
		return bookDetailsService.deleteBookDetails(id);
	}

}
