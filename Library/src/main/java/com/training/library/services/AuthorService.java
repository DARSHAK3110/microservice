package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.entity.Author;
import com.training.library.entity.User;
import com.training.library.repositories.AuthorRepository;
import com.training.library.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private UserRepository userRepository;

	public AuthorResponseDto findAuthor(Long id) {
		Optional<AuthorResponseDto> author = authorRepository.findByAuthorIdAndDeletedAtIsNull(id);
		if (author.isPresent()) {
			return author.get();
		}
		return null;
	}

	public List<AuthorResponseDto> findAuthors() {
		Optional<List<AuthorResponseDto>> authors = authorRepository.findAllByDeletedAtIsNull();
		if (authors.isPresent()) {
			return authors.get();
		}
		return null;
	}

	public List<AuthorResponseDto> saveAuthor(AuthorRequestDto dto, String userName) {
		Author author = new Author();
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong(userName));

		User user = null;
		if (userOptional.isEmpty()) {
			User newUser = new User();
			newUser.setPhone(Long.parseLong(userName));
			user = userRepository.save(newUser);
		} else {
			user = userOptional.get();
		}
		author.setUser(user);
		author.setAuthorDOB(dto.getAuthorDOB());
		author.setAuthorName(dto.getAuthorName());
		authorRepository.save(author);
		return findAuthors();
	}

	public List<AuthorResponseDto> updateAuthor(Long id, AuthorRequestDto dto) {
		Optional<Author> authorOptional = authorRepository.findById(id);
		if (authorOptional.isPresent()) {
			Author author = authorOptional.get();
			author.setAuthorDOB(dto.getAuthorDOB());
			author.setAuthorName(dto.getAuthorName());
			authorRepository.save(author);
		}
		return findAuthors();
	}

	@Transactional
	public List<AuthorResponseDto> deleteAuthor(Long id) {
		authorRepository.deleteByAuthorId(id);
		return findAuthors();
	}

	public Author findAuthorByAuthorId(Long authorId) {
		Optional<Author> author = authorRepository.findById(authorId);
		if (author.isPresent()) {
			return author.get();
		}
		return null;
	}

}
