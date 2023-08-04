package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.Author;
import com.training.library.entity.User;
import com.training.library.repositories.AuthorRepository;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthorServiceTest {

	@Mock
	private AuthorRepository repo;

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private AuthorService service;
	@Autowired
	private EntityGenerator entityGenerator;
	@Mock
	private UserService userService;
	@Mock
	private Environment env;

	@Test
	void findAuthorTest() {
		when(repo.findByAuthorIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(entityGenerator.getAuthorResponseDto(1L)));
		AuthorResponseDto result = service.findAuthor(1L);
		assertThat(result.getAuthorId()).isEqualTo(1L);
	}

	@Test
	void findAuthorTest2() {
		when(repo.findByAuthorIdAndDeletedAtIsNull(0L)).thenReturn(Optional.empty());
		AuthorResponseDto result = service.findAuthor(0L);
		assertThat(result).isNull();
	}

	@Test
	void findAuthorsTest() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<AuthorResponseDto> actual = entityGenerator.getAuthorPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable)).thenReturn(actual);
		Page<AuthorResponseDto> result = service.findAuthors(dto);
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void saveAuthorTest() {
		AuthorRequestDto req = entityGenerator.getAuthorRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(1000000L);
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(repo.save(any(Author.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveAuthor(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveAuthorTest2() {
		AuthorRequestDto req = entityGenerator.getAuthorRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(1000000L);
		when(userService.findByPhone(1231231231L)).thenReturn(null);
		when(userService.newUser(any(String.class))).thenReturn(user);
		when(repo.save(any(Author.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveAuthor(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void updateAuthorTest() {
		Author author = entityGenerator.getMockAuthor();
		AuthorRequestDto req = entityGenerator.getAuthorRequestDto();
		when(repo.findById(0L)).thenReturn(Optional.of(author));
		ResponseEntity<CustomBaseResponseDto> result = service.updateAuthor(0L, req);
		assertThat(result).isNotNull();
	}

	@Test
	void deleteAuthorTest() {
		ResponseEntity<CustomBaseResponseDto> result = service.deleteAuthor(0L);
		assertThat(result).isNotNull();
	}

	@Test
	void findAuthorByAuthorIdTest1() {
		Optional<Author> author = Optional.of(entityGenerator.getMockAuthor());
		when(repo.findById(0L)).thenReturn(author);
		Author result = service.findAuthorByAuthorId(0L);
		assertThat(result.getAuthorId()).isEqualTo(author.get().getAuthorId());
	}

	@Test
	void findAuthorByAuthorIdTest2() {
		when(repo.findById(0L)).thenReturn(Optional.empty());
		Author result = service.findAuthorByAuthorId(0L);
		assertThat(result).isNull();
	}

}
