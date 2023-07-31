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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.view.BookDetailsView;
import com.training.library.services.BookDetailsService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/bookdetails")
public class BookDetailsController {
	@Autowired
	private BookDetailsService bookDetailsService;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	@GetMapping("/book/{id}")
	public ResponseEntity<BookDetailsView> findBookDetails(@PathVariable Long id) {
		BookDetailsView bookDetails = bookDetailsService.findBookDetails(id);
		return ResponseEntity.ok(bookDetails);
	}

	@GetMapping
	public ResponseEntity<Page<BookDetailsView>> findBookDetailss(FilterDto dto) {
		Page<BookDetailsView> bookDetailsPage = bookDetailsService.findAllBookDetails(dto);
		return ResponseEntity.ok(bookDetailsPage);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveBookDetails(@RequestBody BookDetailsRequestDto dto,
			HttpServletRequest req) {
		String userName = req.getHeader("username");
		bookDetailsService.saveBookDetails(dto, userName);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@PutMapping("/book/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateBookDetails(@PathVariable Long id,
			@RequestBody BookDetailsRequestDto dto, HttpServletRequest req) {
		String userName = req.getHeader("username");
		bookDetailsService.updateBookDetails(id, dto, userName);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@DeleteMapping("/book/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBookDetails(@PathVariable Long id) {
		bookDetailsService.deleteBookDetails(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

}
