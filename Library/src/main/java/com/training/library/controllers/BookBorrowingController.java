package com.training.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.services.BookBorrowingService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/library/api/v1/borrowings")
public class BookBorrowingController {
	@Autowired
	private BookBorrowingService bookBorrowingService;

	@GetMapping("/{id}")
	public ResponseEntity<BookBorrowingResponseDto> findBookBorrowing(@PathVariable Long id) {
		BookBorrowingResponseDto bookBorrowing = bookBorrowingService.findBookBorrowing(id);
		return ResponseEntity.ok(bookBorrowing);
	}

	@GetMapping
	public ResponseEntity<List<BookBorrowingResponseDto>> findBookBorrowings() {
		List<BookBorrowingResponseDto> bookBorrowingList = bookBorrowingService.findAllBookBorrowing();
		return ResponseEntity.ok(bookBorrowingList);
	}

	@PostMapping
	public ResponseEntity<List<BookBorrowingResponseDto>> saveBookBorrowing(
			@RequestBody BookBorrowingRequestDto dto, HttpServletRequest req) {
		String userName = req.getHeader("username");
		List<BookBorrowingResponseDto> bookBorrowingList = bookBorrowingService.saveBookBorrowing(dto,
				userName);
		return ResponseEntity.ok(bookBorrowingList);
	}

	@PutMapping("/{id}")
	public ResponseEntity<List<BookBorrowingResponseDto>> updateBookBorrowing(@PathVariable Long id,
			@RequestBody BookBorrowingRequestDto dto) {
		List<BookBorrowingResponseDto> bookBorrowingList = bookBorrowingService.updateBookBorrowing(id, dto);
		return ResponseEntity.ok(bookBorrowingList);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<List<BookBorrowingResponseDto>> deleteBookBorrowing(@PathVariable Long id) {
		List<BookBorrowingResponseDto> bookBorrowingList = bookBorrowingService.deleteBookBorrowing(id);
		return ResponseEntity.ok(bookBorrowingList);
	}

}
