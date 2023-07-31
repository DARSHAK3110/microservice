package com.training.library.controllers;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.BookStatusService;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/bookstatuses")
@PropertySource("classpath:message.properties")
public class BookStatusController {
	@Autowired
	private BookStatusService bookStatusService;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	
	@GetMapping("/bookstatus/{id}")
	public ResponseEntity<BookStatusResponseDto> findBookStatus(@PathVariable Long id) {
		BookStatusResponseDto bookStatus = bookStatusService.findBookStatus(id);
		
		return ResponseEntity.ok(bookStatus);
	} 

	@GetMapping("/book/{id}")
	public ResponseEntity<Page<BookStatusResponseDto>>findBookStatusByBookDetails(@PathVariable Long id, FilterDto dto)  {
		Page<BookStatusResponseDto> bookStatusPage = bookStatusService.findAllBookStatusByBookDetailsId(dto,id);
		return ResponseEntity.ok(bookStatusPage);
	}
	

	
	@PutMapping("/bookstatus/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateBookDetails(@PathVariable Long id,
			@RequestBody BookStatusRequestDto dto) {
		bookStatusService.updateBookStatus(id, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
	
	@DeleteMapping("/bookstatus/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBookDetails(@PathVariable Long id) {
		bookStatusService.deleteBookStatus(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

}
