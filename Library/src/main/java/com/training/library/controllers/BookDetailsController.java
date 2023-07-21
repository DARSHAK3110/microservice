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

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.view.BookDetailsView;
import com.training.library.services.BookDetailsService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/library/api/v1/bookdetails")
public class BookDetailsController {
	@Autowired
	private BookDetailsService bookDetailsService;

	@GetMapping("/{id}")
	public ResponseEntity<BookDetailsView> findBookDetails(@PathVariable Long id) {
		BookDetailsView bookDetails = bookDetailsService.findBookDetails(id);
		return ResponseEntity.ok(bookDetails);
	}
	
	@GetMapping
	public ResponseEntity<List<BookDetailsView>> findBookDetailss() {
		List<BookDetailsView> bookDetailsList = bookDetailsService.findAllBookDetails();
		return ResponseEntity.ok(bookDetailsList);
	}
	

	@PostMapping
	public ResponseEntity<List<BookDetailsView>> saveBookDetails(@RequestBody BookDetailsRequestDto dto,
			HttpServletRequest req) {
		String userName = req.getHeader("username");
		List<BookDetailsView> bookDetailsList = bookDetailsService.saveBookDetails(dto, userName);
		return ResponseEntity.ok(bookDetailsList);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<List<BookDetailsView>> updateBookDetails(@PathVariable Long id,
			@RequestBody BookDetailsRequestDto dto, HttpServletRequest req) {
		String userName = req.getHeader("username");
		List<BookDetailsView> bookDetailsList = bookDetailsService.updateBookDetails(id, dto, userName);
		return ResponseEntity.ok(bookDetailsList);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<List<BookDetailsView>> deleteBookDetails(@PathVariable Long id) {
		List<BookDetailsView> bookDetailsList = bookDetailsService.deleteBookDetails(id);
		return ResponseEntity.ok(bookDetailsList);
	}

}
