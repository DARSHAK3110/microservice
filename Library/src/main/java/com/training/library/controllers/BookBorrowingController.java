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

import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.BookBorrowingService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/borrowings")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BookBorrowingController {
	@Autowired
	private BookBorrowingService bookBorrowingService;


	@GetMapping("bookborrowing/{id}")
	public ResponseEntity<BookBorrowingResponseDto> findBookBorrowing(@PathVariable Long id) {
		BookBorrowingResponseDto bookBorrowing = bookBorrowingService.findBookBorrowing(id);
		return ResponseEntity.ok(bookBorrowing);
	}

	@GetMapping("/bookstatus/{id}")
	public ResponseEntity<BookBorrowingResponseDto> findBookBorrowingByBookStatus(@PathVariable Long id) {
		BookBorrowingResponseDto bookBorrowing = bookBorrowingService.findBookBorrowingByBookStatus(id);
		return ResponseEntity.ok(bookBorrowing);
	}

	@GetMapping
	public ResponseEntity<Page<BookBorrowingResponseDto>> findBookBorrowings(FilterDto dto) {
		Page<BookBorrowingResponseDto> bookBorrowingList = bookBorrowingService.findAllBookBorrowing(dto);
		return ResponseEntity.ok(bookBorrowingList);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveBookBorrowing(@RequestBody BookBorrowingRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookBorrowingService.saveBookBorrowing(dto, userName);
	}

	@DeleteMapping("bookborrowing/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBookBorrowing(@PathVariable Long id) {
		return bookBorrowingService.deleteBookBorrowing(id);
	}
}
