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

import com.training.library.dto.response.SectionResponseDto;
import com.training.library.entity.Section;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SectionRepositoryTest {
	
	@Autowired
	private SectionRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;
	private Long id;
	
	@Test
	void testFindBySectionIdAndDeletedAtIsNull() {
		Optional<SectionResponseDto> result = repo.findBySectionIdAndDeletedAtIsNull(id);
		assertThat(result.get().getSectionId()).isEqualTo(id);
	}
	
	@Test
	void testFailFindBySectionIdAndDeletedAtIsNull() {
		repo.deleteById(id);
		Optional<SectionResponseDto> result = repo.findBySectionIdAndDeletedAtIsNull(id);
		assertThat(result).isEmpty();
	}
	@Test
	void testFindAllByDeletedAtIsNull() {
		Page<SectionResponseDto> result = repo.findAllByDeletedAtIsNull("", PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testDeleteBySectionId() {
		repo.deleteById(id);
		Optional<SectionResponseDto> authorOtional= repo.findBySectionIdAndDeletedAtIsNull(id);
		assertThat(authorOtional).isEmpty();
	}

	@Test
	void testGetAllByFloor_FloorNoAndDeletedAtIsNotNull() {
		List<SectionResponseDto> result = repo.getAllByFloor_FloorNoAndDeletedAtIsNotNull(0L);
		assertThat(result).isEmpty();
	}

	@BeforeEach
	void newSection() {
		Section savedSection = repo.save(entityGenerator.getSection());
		id = savedSection.getSectionId();
	}
}
