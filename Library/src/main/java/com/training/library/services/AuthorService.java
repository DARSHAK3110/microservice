package com.training.library.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.Author;
import com.training.library.entity.User;
import com.training.library.repositories.AuthorRepository;

import jakarta.transaction.Transactional;

@Service
@PropertySource("classpath:message.properties")
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private UserService userService;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	public AuthorResponseDto findAuthor(Long id) {
		Optional<AuthorResponseDto> author = authorRepository.findByAuthorIdAndDeletedAtIsNull(id);
		if (author.isPresent()) {
			return author.get();
		}
		return null;
	}

	public Page<AuthorResponseDto> findAuthors(FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return authorRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageble);
	}

	public ResponseEntity<CustomBaseResponseDto> saveAuthor(AuthorRequestDto dto, String userName) {
		Author author = new Author();
		User user = userService.findByPhone(Long.parseLong(userName));

		if (user == null) {
			user = userService.newUser(userName);
		}
		author.setUser(user);
		author.setAuthorDOB(dto.getAuthorDOB());
		author.setAuthorName(dto.getAuthorName());
		authorRepository.save(author);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public ResponseEntity<CustomBaseResponseDto> updateAuthor(Long id, AuthorRequestDto dto) {
		Optional<Author> authorOptional = authorRepository.findById(id);
		if (authorOptional.isPresent()) {
			Author author = authorOptional.get();
			author.setAuthorDOB(dto.getAuthorDOB());
			author.setAuthorName(dto.getAuthorName());
			authorRepository.save(author);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteAuthor(Long id) {
		authorRepository.deleteByAuthorId(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public Author findAuthorByAuthorId(Long authorId) {
		Optional<Author> author = authorRepository.findById(authorId);
		if (author.isPresent()) {
			return author.get();
		}
		return null;
	}

}
