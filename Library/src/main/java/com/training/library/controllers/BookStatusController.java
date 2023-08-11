package com.training.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.BookStatusService;

import jakarta.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/bookstatuses")
@PropertySource("classpath:message.properties")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BookStatusController {
	@Autowired
	private BookStatusService bookStatusService;

	@GetMapping("/bookstatus/{id}")
	public ResponseEntity<BookStatusResponseDto> findBookStatus(@PathVariable Long id) {
		BookStatusResponseDto bookStatus = bookStatusService.findBookStatus(id);
		return ResponseEntity.ok(bookStatus);
	}

	@GetMapping("/book/{id}")
	public ResponseEntity<Page<BookStatusResponseDto>> findBookStatusByBookDetails(@PathVariable Long id,
			FilterDto dto) {
		Page<BookStatusResponseDto> bookStatusPage = bookStatusService.findAllBookStatusByBookDetailsId(dto, id);
		return ResponseEntity.ok(bookStatusPage);
	}

	@PutMapping("/bookstatus/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateBookStatus(@PathVariable Long id,
			@Valid @RequestBody BookStatusRequestDto dto) {
		return bookStatusService.updateBookStatus(id, dto);
	}

	@DeleteMapping("/bookstatus/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBookStatus(@PathVariable Long id) {
		return bookStatusService.deleteBookStatus(id);
	}

	@GetMapping("/locationcounter/{id}")
	public ResponseEntity<CustomBaseResponseDto> checkBooksByLocation(@PathVariable Long id) {
		return bookStatusService.getCountByLocation(id);
	}

	@GetMapping("/shelfcounter/{id}")
	public ResponseEntity<CustomBaseResponseDto> checkBooksByShelf(@PathVariable Long id) {
		return bookStatusService.getCountByShelf(id);
	}

	@GetMapping("/sectioncounter/{id}")
	public ResponseEntity<CustomBaseResponseDto> checkBooksBySection(@PathVariable Long id) {
		return bookStatusService.getCountBySection(id);
	}

	@GetMapping("/floorcounter/{id}")
	public ResponseEntity<CustomBaseResponseDto> checkBooksByFloor(@PathVariable Long id) {
		return bookStatusService.getCountByFloor(id);
	}
	
	@GetMapping("/location/{id}")
	public ResponseEntity<Page<BookStatusResponseDto>> findAllBooksByLocation(@PathVariable Long id, FilterDto dto) {
		Page<BookStatusResponseDto> bookStatusPage = bookStatusService.findAllBooksByLocation(id,dto);
		return ResponseEntity.ok(bookStatusPage);
	}
	
	@GetMapping("/shelf/{id}")
	public ResponseEntity<Page<BookStatusResponseDto>> findAllBooksByShelf(@PathVariable Long id, FilterDto dto) {
		Page<BookStatusResponseDto> bookStatusPage = bookStatusService.findAllBooksByShelf(id,dto);
		return ResponseEntity.ok(bookStatusPage);
	}
	
	@GetMapping("/section/{id}")
	public ResponseEntity<Page<BookStatusResponseDto>> findAllBooksBySection(@PathVariable Long id, FilterDto dto) {
		Page<BookStatusResponseDto> bookStatusPage = bookStatusService.findAllBooksBySection(id,dto);
		return ResponseEntity.ok(bookStatusPage);
	}
	
	@GetMapping("/floor/{id}")
	public ResponseEntity<Page<BookStatusResponseDto>> findAllBooksByFloor(@PathVariable Long id, FilterDto dto) {
		Page<BookStatusResponseDto> bookStatusPage = bookStatusService.findAllBooksByFloor(id,dto);
		return ResponseEntity.ok(bookStatusPage);
	}
}
