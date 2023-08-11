package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.dto.response.FloorResponseDto;
import com.training.library.entity.Floor;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FloorRepositoryTest {
	
	@Autowired
	private EntityGenerator entityGenerator;
	@Autowired
	private FloorRepository repo;
	private Long id;

	@Test
	void testFindByFloorIdAndDeletedAtIsNull() {
		Optional<FloorResponseDto> floorResult = repo.findByFloorIdAndDeletedAtIsNull(id);
		assertThat(floorResult.get().getFloorId()).isEqualTo(id);
	}
	
	@Test
	void testFailFindByFloorIdAndDeletedAtIsNull() {
		repo.deleteById(id);
		Optional<FloorResponseDto> floorResult = repo.findByFloorIdAndDeletedAtIsNull(id);
		assertThat(floorResult.isEmpty()).isEqualTo(true);
	}

	@Test
	void testFindByFloorNo() {
		Optional<Floor> floorResult = repo.findByFloorNo(1000000L);
		assertThat(floorResult.get().getFloorId()).isEqualTo(id);
	}

	@Test
	void testFailFindByFloorNo() {
		Optional<Floor> floorResult = repo.findByFloorNo(10000L);
		assertThat(floorResult.isEmpty()).isEqualTo(true);
	}
	
	@Test
	void testFindAllByDeletedAtIsNull() {
		Page<FloorResponseDto> result = repo.findAllByDeletedAtIsNull("", PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testDeleteByFloorId() {
		repo.deleteById(id);
		Optional<FloorResponseDto> authorOtional= repo.findByFloorIdAndDeletedAtIsNull(id);
		assertThat(authorOtional).isEmpty();
	}

	@BeforeEach
	void newFloor() {
		Floor floor = repo.save(entityGenerator.getFloor());
		id = floor.getFloorId();
	}


}
