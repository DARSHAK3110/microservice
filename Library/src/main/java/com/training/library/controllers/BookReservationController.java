package com.training.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.BookReservationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/reservations")
@PropertySource("classpath:message.properties")
public class BookReservationController {
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
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
		String userName = req.getHeader("username");
		bookReservationService.saveBookReservation(dto,
				userName);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@PutMapping("/reservaion/{id}")
	public ResponseEntity<CustomBaseResponseDto>  updateBookReservation(@PathVariable Long id,
			@RequestBody BookReservationRequestDto dto) {
		bookReservationService.updateBookReservation(id, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@DeleteMapping("/reservaion/{id}")
	public ResponseEntity<CustomBaseResponseDto>  deleteBookReservation(@PathVariable Long id) {
		bookReservationService.deleteBookReservation(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

}
