package com.training.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.view.BookStatusView;
import com.training.library.services.BookStatusService;

@Controller
@RequestMapping("/library/api/v1/bookstatus")
public class BookStatusController {
	@Autowired
	private BookStatusService bookStatusService;

	@GetMapping("/{id}")
	public ResponseEntity<BookStatusView> findBookStatus(@PathVariable Long id) {
		BookStatusView bookStatus = bookStatusService.findBookStatus(id);
		return ResponseEntity.ok(bookStatus);
	}

	@GetMapping
	public ResponseEntity<List<BookStatusView>> findBookStatuss() {
		List<BookStatusView> bookStatusList = bookStatusService.findAllBookStatus();
		return ResponseEntity.ok(bookStatusList);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<List<BookStatusView>> updateBookDetails(@PathVariable Long id,
			@RequestBody BookStatusRequestDto dto) {
		List<BookStatusView> bookStatusList = bookStatusService.updateBookStatus(id, dto);
		return ResponseEntity.ok(bookStatusList);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<List<BookStatusView>> deleteBookDetails(@PathVariable Long id) {
		List<BookStatusView> bookStatusList = bookStatusService.deleteBookStatus(id);
		return ResponseEntity.ok(bookStatusList);
	}

}
