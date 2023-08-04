package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.entity.BookDetails;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookDetailsRepositoryTest {

	@Autowired
	private BookDetailsRepository repo;
	private Long id;
	@Autowired
	private EntityGenerator entityGenerator;

	@BeforeEach
	void setUp() throws Exception {

		BookDetails savedBookDetails = repo.save(entityGenerator.getBookDetails());
		id = savedBookDetails.getBookDetailsId();
	}

	@Test
	void testFindByBookDetailsIdAndDeletedAtIsNull() {
		Optional<BookDetailsResponseDto> floorResult = repo.findByBookDetailsIdAndDeletedAtIsNull(id);
		assertThat(floorResult.get().getBookDetailsId()).isEqualTo(id);
	}

	@Test
	void testFailFindByBookDetailsIdAndDeletedAtIsNull() {
		repo.deleteById(id);
		Optional<BookDetailsResponseDto> floorResult = repo.findByBookDetailsIdAndDeletedAtIsNull(id);
		assertThat(floorResult.isEmpty()).isEqualTo(true);
	}

	@Test
	void testFindAllByDeletedAtIsNullAndTitleIgnoreCaseContainingOrAuthor_AuthorNameIgnoreCaseContaining() {
		Page<BookDetailsResponseDto> result = repo
				.findAllByDeletedAtIsNullAndTitleIgnoreCaseContainingOrAuthor_AuthorNameIgnoreCaseContaining("", "",
						PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testDeleteByBookDetailsId() {
		repo.deleteByBookDetailsId(id);
		Optional<BookDetails> result = repo.findById(id);
		assertThat(result.get().getDeletedAt() != null);
	}

	@Test
	void testFindByIsbn() {
		Optional<BookDetails> result = repo.findByIsbn(9999999999999L);
		assertThat(result.get().getBookDetailsId()).isEqualTo(id);
	}

}
