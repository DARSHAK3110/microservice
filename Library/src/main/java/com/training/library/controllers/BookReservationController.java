package com.training.library.controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.BookReservationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/reservations")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BookReservationController {
	@Autowired
	private BookReservationService bookReservationService;

	@GetMapping("/reservaion/{id}")
	public ResponseEntity<BookReservationResponseDto> findBookReservation(@PathVariable Long id) {
		BookReservationResponseDto bookReservation = bookReservationService.findBookReservation(id);
		return ResponseEntity.ok(bookReservation);
	}

	@GetMapping
	public ResponseEntity<Page<BookReservationResponseDto>> findBookReservations(FilterDto dto) {
		Page<BookReservationResponseDto> bookReservationList = bookReservationService.findAllBookReservation(dto);
		return ResponseEntity.ok(bookReservationList);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveBookReservation(
			@RequestBody BookReservationRequestDto dto, HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookReservationService.saveBookReservation(dto,
				userName);
	}


	@DeleteMapping("/reservaion/{id}")
	public ResponseEntity<CustomBaseResponseDto>  deleteBookReservation(@PathVariable Long id) {
		return bookReservationService.deleteBookReservation(id);
	}

}
