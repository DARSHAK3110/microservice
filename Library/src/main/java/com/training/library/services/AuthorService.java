package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.request.FilterDto;
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

	public Page<AuthorResponseDto> findAuthors(FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return authorRepository.findAllByDeletedAtIsNull(dto.getSearch(),  pageble);
	}

	public void saveAuthor(AuthorRequestDto dto, String userName) {
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
	}

	public void updateAuthor(Long id, AuthorRequestDto dto) {
		Optional<Author> authorOptional = authorRepository.findById(id);
		if (authorOptional.isPresent()) {
			Author author = authorOptional.get();
			author.setAuthorDOB(dto.getAuthorDOB());
			author.setAuthorName(dto.getAuthorName());
			authorRepository.save(author);
		}
	}

	@Transactional
	public void deleteAuthor(Long id) {
		authorRepository.deleteByAuthorId(id);
	}

	public Author findAuthorByAuthorId(Long authorId) {
		Optional<Author> author = authorRepository.findById(authorId);
		if (author.isPresent()) {
			return author.get();
		}
		return null;
	}

}
