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

import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.services.BookReservationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/library/api/v1/reservaions")
public class BookReservationController {
	@Autowired
	private BookReservationService bookReservationService;

	@GetMapping("/{id}")
	public ResponseEntity<BookReservationResponseDto> findBookReservation(@PathVariable Long id) {
		BookReservationResponseDto bookReservation = bookReservationService.findBookReservation(id);
		return ResponseEntity.ok(bookReservation);
	}

	@GetMapping
	public ResponseEntity<List<BookReservationResponseDto>> findBookReservations() {
		List<BookReservationResponseDto> bookReservationList = bookReservationService.findAllBookReservation();
		return ResponseEntity.ok(bookReservationList);
	}

	@PostMapping
	public ResponseEntity<List<BookReservationResponseDto>> saveBookReservation(
			@RequestBody BookReservationRequestDto dto, HttpServletRequest req) {
		String userName = req.getHeader("username");
		List<BookReservationResponseDto> bookReservationList = bookReservationService.saveBookReservation(dto,
				userName);
		return ResponseEntity.ok(bookReservationList);
	}

	@PutMapping("/{id}")
	public ResponseEntity<List<BookReservationResponseDto>> updateBookReservation(@PathVariable Long id,
			@RequestBody BookReservationRequestDto dto) {
		List<BookReservationResponseDto> bookReservationList = bookReservationService.updateBookReservation(id, dto);
		return ResponseEntity.ok(bookReservationList);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<List<BookReservationResponseDto>> deleteBookReservation(@PathVariable Long id) {
		List<BookReservationResponseDto> bookReservationList = bookReservationService.deleteBookReservation(id);
		return ResponseEntity.ok(bookReservationList);
	}

}
