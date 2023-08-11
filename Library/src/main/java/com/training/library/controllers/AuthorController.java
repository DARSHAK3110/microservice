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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.AuthorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/authors")
public class AuthorController {
	@Autowired
	private AuthorService authorService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("author/{id}")
	public ResponseEntity<AuthorResponseDto> findAuthor(@PathVariable Long id) {

		AuthorResponseDto author = authorService.findAuthor(id);
		return ResponseEntity.ok(author);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Page<AuthorResponseDto>> findAuthors(FilterDto dto) {
		
		Page<AuthorResponseDto> authors = authorService.findAuthors(dto);
		return ResponseEntity.ok(authors);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveAuthor(@Valid @RequestBody AuthorRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return authorService.saveAuthor(dto, userName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("author/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateAuthor(@PathVariable Long id,
			@Valid @RequestBody AuthorRequestDto dto) {
		return authorService.updateAuthor(id, dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("author/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteAuthor(@PathVariable Long id) {
		return authorService.deleteAuthor(id);
	}

}
