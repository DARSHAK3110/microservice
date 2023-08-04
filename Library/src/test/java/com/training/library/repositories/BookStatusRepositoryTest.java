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

import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.entity.BookStatus;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookStatusRepositoryTest {

	@Autowired
	private BookStatusRepository repo;
	private Long id;
	private Long bookDetailsId;
	@Autowired
	private EntityGenerator entityGenerator;

	@Transactional
	@BeforeEach
	void setUp() throws Exception {

		BookStatus savedBookStatus = entityGenerator.getBookStatus();
		id = savedBookStatus.getBookStatusId();
		bookDetailsId = savedBookStatus.getBookDetails().getBookDetailsId();
	}

	@Test
	void testFindByBookStatusIdAndDeletedAtIsNull() {
		Optional<BookStatusResponseDto> result = repo.findByBookStatusIdAndDeletedAtIsNull(id);
		assertThat(result.get().getBookStatusId()).isEqualTo(id);
	}

	@Test
	void testFailFindByBookStatusIdAndDeletedAtIsNull() {
		Optional<BookStatusResponseDto> result = repo.findByBookStatusIdAndDeletedAtIsNull(id);
		assertThat(result.get().getBookStatusId()).isEqualTo(id);
	}

	@Test
	void testDeleteByBookStatusId() {
		repo.deleteByBookStatusId(id);
		Optional<BookStatusResponseDto> result = repo.findByBookStatusIdAndDeletedAtIsNull(id);
		assertThat(result).isEmpty();
	}

	@Test
	void testFindAllByDeletedAtIsNullAndBookDetails_BookDetailsIdPageableLong() {
		Page<BookStatusResponseDto> result = repo
				.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(PageRequest.of(0, 2), bookDetailsId);
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testAvailableFindAllByDeletedAtIsNullAndBookDetails_BookDetailsIdPageableLongBoolean() {
		Page<BookStatusResponseDto> result = repo
				.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(PageRequest.of(0, 2), bookDetailsId, true);
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testNotAvailableFindAllByDeletedAtIsNullAndBookDetails_BookDetailsIdPageableLongBoolean() {
		Page<BookStatusResponseDto> result = repo
				.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(PageRequest.of(0, 2), bookDetailsId, false);
		assertThat(result.getContent().size()).isNotPositive();
	}
}
