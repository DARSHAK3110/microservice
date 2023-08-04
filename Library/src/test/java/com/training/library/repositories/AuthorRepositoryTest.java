package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.entity.Author;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorRepositoryTest {

	@Autowired
	private AuthorRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;
	private Long id;
	
	@Test
	@Order(1)
	void testFindByAuthorIdAndDeletedAtIsNull() {
		Optional<AuthorResponseDto> result = repo.findByAuthorIdAndDeletedAtIsNull(id);
			assertThat(result.get().getAuthorId()).isEqualTo(id);			
	}

	@Test
	@Order(2)
	void testFindAllByDeletedAtIsNull() {
		Page<AuthorResponseDto> result = repo.findAllByDeletedAtIsNull("", PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	@Order(3)
	@Transactional
	void testDeleteByAuthorId() {
	repo.deleteByAuthorId(id);
		Optional<AuthorResponseDto> authorOtional= repo.findByAuthorIdAndDeletedAtIsNull(id);
			assertThat(authorOtional).isEmpty();
	}
	@Test
	@Order(4)
	void testFailFindByAuthorIdAndDeletedAtIsNull() {
		Optional<Author> authorOptional= repo.findById(id);
		if(authorOptional.isPresent()) {
			Author author = authorOptional.get();
			author.setDeletedAt(new Date(System.currentTimeMillis()));
			repo.save(author);
			Optional<AuthorResponseDto> result = repo.findByAuthorIdAndDeletedAtIsNull(id);
			assertThat(result).isEmpty();
		}
	}
@BeforeEach
	void newAuthor(){
		Author save = entityGenerator.getAuthor();
		this.id = save.getAuthorId();	
	}
}
