package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

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

import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.entity.BookReservation;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookReservationRepositoryTest {

	@Autowired
	private BookReservationRepository repo;
	private Long id;
	
	@Autowired
	private EntityGenerator entityGenerator;
	@BeforeEach
	void setUp() throws Exception {
		BookReservation savedReservation = repo.save(entityGenerator.getBookReservation());
		id = savedReservation.getBookReservationId();
	}

	@Order(1)
	@Test
	void testFindByBookReservationIdAndDeletedAtIsNull() {
		Optional<BookReservationResponseDto> result = repo.findByBookReservationIdAndDeletedAtIsNull(id);
			assertThat(result.get().getBookReservationId()).isEqualTo(id);			
	}
	@Order(2)
	@Test
	void testFailFindByBookReservationIdAndDeletedAtIsNull() {
		repo.deleteByBookReservationId(id);
		Optional<BookReservationResponseDto> result = repo.findByBookReservationIdAndDeletedAtIsNull(id);
		assertThat(result).isEmpty();
	}
	@Order(3)
	@Test
	void testFindAllByDeletedAtIsNull() {
		Page<BookReservationResponseDto> result = repo.findAllByDeletedAtIsNull("", PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}
	@Order(4)
	@Test
	void testDeleteByBookReservationId() {
		repo.deleteByBookReservationId(id);
		Optional<BookReservationResponseDto> authorOtional= repo.findByBookReservationIdAndDeletedAtIsNull(id);
		assertThat(authorOtional).isEmpty();
	}

}
