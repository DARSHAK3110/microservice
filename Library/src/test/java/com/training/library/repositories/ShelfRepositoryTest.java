package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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

import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.entity.Shelf;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShelfRepositoryTest {
	@Autowired
	private EntityGenerator entityGenerator;
	@Autowired
	private ShelfRepository repo;
	private Long id;

	@BeforeEach
	void setUp() throws Exception {

		Shelf savedShelf = repo.save(entityGenerator.getShelf());
		id = savedShelf.getShelfId();
	}

	@Test
	void testFindByShelfIdAndDeletedAtIsNull() {
		Optional<ShelfResponseDto> result = repo.findByShelfIdAndDeletedAtIsNull(id);
		assertThat(result.get().getShelfId()).isEqualTo(id);
	}

	@Test
	void testFailFindByShelfIdAndDeletedAtIsNull() {
		repo.deleteByShelfId(id);
		Optional<ShelfResponseDto> result = repo.findByShelfIdAndDeletedAtIsNull(id);
		assertThat(result.isEmpty()).isEqualTo(true);
	}

	@Test
	void testFindAllByDeletedAtIsNull() {
		Page<ShelfResponseDto> result = repo.findAllByDeletedAtIsNull("", PageRequest.of(0, 2));
		assertThat(result.getNumberOfElements() > 0).isTrue();
	}

	@Test
	void testDeleteByShelfId() {
		repo.deleteByShelfId(id);
		Optional<ShelfResponseDto> shelfOtional = repo.findByShelfIdAndDeletedAtIsNull(id);
		assertThat(shelfOtional).isEmpty();

	}

	@Test
	void testGetAllBySection_SectionIdAndDeletedAtIsNotNull() {
		List<ShelfResponseDto> result = repo.getAllBySection_SectionIdAndDeletedAtIsNotNull(0L);
		assertThat(result).isEmpty();
	}

}
