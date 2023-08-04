package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.entity.BookBorrowing;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BookBorrowingRepositoryTest {
	@Autowired
	private EntityGenerator entityGenerator;
	@Autowired
	private BookBorrowingRepository repo;
	private Long id;
	private Long bookStatusId;
	@BeforeEach
	void setUp() throws Exception {
		BookBorrowing savedBorrowing = repo.save(entityGenerator.getBookBorrowing());
		id = savedBorrowing.getBookBorrowingId();
		bookStatusId = savedBorrowing.getBookStatus().getBookStatusId();
	}

	@Test
	void testFindByBookBorrowingIdAndDeletedAtIsNull() {
		Optional<BookBorrowingResponseDto> result = repo.findByBookBorrowingIdAndDeletedAtIsNull(id);
		assertThat(result.get().getBookBorrowingId()).isEqualTo(id);
	}

	@Test
	void testFailFindByBookBorrowingIdAndDeletedAtIsNull() {
		repo.deleteByBookBorrowingId(id);
		Optional<BookBorrowingResponseDto> result = repo.findByBookBorrowingIdAndDeletedAtIsNull(id);
		assertThat(result).isEmpty();
	}
	@Test
	void testFindAllwithSearch() {
		Page<BookBorrowingResponseDto> result = repo.findAllwithSearch("", PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testDeleteByBookBorrowingId() {
		repo.deleteByBookBorrowingId(id);
		Optional<BookBorrowingResponseDto> authorOtional= repo.findByBookBorrowingIdAndDeletedAtIsNull(id);
		assertThat(authorOtional).isEmpty();
	}

	@Test
	void testFindByBookStatus_BookStatusIdAndDeletedAtIsNull() {
		Optional<BookBorrowingResponseDto> result = repo.findByBookStatus_BookStatusIdAndDeletedAtIsNull(bookStatusId);
		assertThat(result.get().getBookId()).isEqualTo(bookStatusId);
	}

}
