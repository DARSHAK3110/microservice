package com.training.library.controllers;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.EmailRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.ReservationStatusDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.BookReservationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/reservations")
public class BookReservationController {
	@Autowired
	private BookReservationService bookReservationService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/reservation/{id}")
	public ResponseEntity<BookReservationResponseDto> findBookReservation(@PathVariable Long id) {
		BookReservationResponseDto bookReservation = bookReservationService.findBookReservation(id);
		return ResponseEntity.ok(bookReservation);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping
	public ResponseEntity<Page<BookReservationResponseDto>> findBookReservations(FilterDto dto) throws NumberFormatException, ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Page<BookReservationResponseDto> bookReservationList = bookReservationService.findAllBookReservation(dto,userName);
		return ResponseEntity.ok(bookReservationList);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/reserver/{id}")
	public ResponseEntity<Boolean> checkReserver(@PathVariable("id") Long id, @RequestParam Long bookStatusId){
		Boolean result = bookReservationService.checkReserverByBookStatusId(id,bookStatusId);
		return ResponseEntity.ok(result);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveBookReservation(@Valid @RequestBody BookReservationRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Boolean isUser = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
		
		return bookReservationService.saveBookReservation(dto, userName,isUser);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/status/{id}")
	public ResponseEntity<CustomBaseResponseDto> saveBookReservationStatus(@PathVariable("id") Long id, @Valid @RequestBody ReservationStatusDto dto) {
		return bookReservationService.saveBookReservationStatus(id,dto.getStatus(),dto.getBookStatusId(),dto.getEmail());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/mail/{id}")
	public ResponseEntity<CustomBaseResponseDto> sendMailBookReservationStatus(@PathVariable("id") Long id, @Valid @RequestBody EmailRequestDto dto) {
		return bookReservationService.sendMail(id,dto);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping("/reservationcounter/{id}")
	public ResponseEntity<Boolean> countBookReservationByUserPhone(@PathVariable Long id) {
		Boolean bookReservation = bookReservationService.countBookReservationByUserPhone(id);
		return ResponseEntity.ok(bookReservation);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/reservation/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBookReservation(@PathVariable Long id) {
		return bookReservationService.deleteBookReservation(id);
	}

}
