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

import com.training.library.dto.response.LocationResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.Location;
import com.training.library.entity.Section;
import com.training.library.entity.Shelf;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationRepositoryTest {

	private Long id;
	@Autowired
	private LocationRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;

	@BeforeEach
	void setUp() throws Exception {
		Location savedLocation = repo.save(entityGenerator.getLocation());
		id = savedLocation.getLocationId();
	}

	@Test
	void testFindByLocationIdAndDeletedAtIsNull() {
		Optional<LocationResponseDto> result = repo.findByLocationIdAndDeletedAtIsNull(id);
		if (result.isPresent()) {
			assertThat(result.get().getLocationId()).isEqualTo(id);
		}
	}

	@Test
	void testFailFindByLocationIdAndDeletedAtIsNull() {
		repo.deleteById(id);
		Optional<LocationResponseDto> result = repo.findByLocationIdAndDeletedAtIsNull(id);
		assertThat(result.isEmpty()).isEqualTo(true);
	}

	@Test
	void testFindAllByDeletedAtIsNull() {
		Page<LocationResponseDto> result = repo.findAllByDeletedAtIsNull("", PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testDeleteByLocationId() {
		repo.deleteById(id);
		Optional<LocationResponseDto> locationOtional = repo.findByLocationIdAndDeletedAtIsNull(id);
		assertThat(locationOtional).isEmpty();
	}

	@Test
	void testGetAllByShelf_ShelfIdAndDeletedAtIsNotNull() {
		List<LocationResponseDto> result = repo.getAllByShelf_ShelfIdAndDeletedAtIsNotNull(0L);
		assertThat(result).isEmpty();
	}

}
