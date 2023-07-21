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

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.services.AuthorService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/library/api/v1/authors")
public class AuthorController {
	@Autowired
	private AuthorService authorService;

	@GetMapping("/{id}")
	public ResponseEntity<AuthorResponseDto> findAuthor(@PathVariable Long id) {

		AuthorResponseDto author = authorService.findAuthor(id);
		return ResponseEntity.ok(author);
	}

	@GetMapping
	public ResponseEntity<List<AuthorResponseDto>> findAuthors() {
		List<AuthorResponseDto> authors = authorService.findAuthors();
		return ResponseEntity.ok(authors);
	}

	@PostMapping
	public ResponseEntity<List<AuthorResponseDto>> saveAuthor(@RequestBody AuthorRequestDto dto,
			HttpServletRequest req) {
		String username = req.getHeader("username");
		List<AuthorResponseDto> authors = authorService.saveAuthor(dto, username);
		return ResponseEntity.ok(authors);
	}

	@PutMapping("/{id}")
	public ResponseEntity<List<AuthorResponseDto>> updateAuthor(@PathVariable Long id,
			@RequestBody AuthorRequestDto dto) {
		List<AuthorResponseDto> authors = authorService.updateAuthor(id, dto);
		return ResponseEntity.ok(authors);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<List<AuthorResponseDto>> deleteAuthor(@PathVariable Long id) {
		List<AuthorResponseDto> authors = authorService.deleteAuthor(id);
		return ResponseEntity.ok(authors);
	}
}
