package com.training.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.external.modal.UserResponseDto;
import com.training.library.services.AuthorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@CrossOrigin
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/authors")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AuthorController {
	@Autowired
	private AuthorService authorService;
	
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	@GetMapping("author/{id}")
	public ResponseEntity<AuthorResponseDto> findAuthor(@PathVariable Long id) {

		AuthorResponseDto author = authorService.findAuthor(id);
		return ResponseEntity.ok(author);
	}

	@GetMapping
	public ResponseEntity<Page<AuthorResponseDto>> findAuthors(FilterDto dto, @RequestHeader("Authorization") String token) {
		Page<AuthorResponseDto> authors = authorService.findAuthors(dto);
		return ResponseEntity.ok(authors);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveAuthor(@Valid @RequestBody AuthorRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		authorService.saveAuthor(dto, userName);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@PutMapping("author/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateAuthor(@PathVariable Long id,
			@Valid @RequestBody AuthorRequestDto dto) {
		authorService.updateAuthor(id, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@DeleteMapping("author/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteAuthor(@PathVariable Long id) {
		authorService.deleteAuthor(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
}
