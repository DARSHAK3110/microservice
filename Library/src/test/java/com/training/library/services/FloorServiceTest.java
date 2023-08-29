package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.FloorRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.FloorResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.User;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.FloorRepository;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FloorServiceTest {

	@Mock
	private FloorRepository repo;

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private FloorService service;
	@Autowired
	private EntityGenerator entityGenerator;
	@Mock
	private UserService userService;
	@Mock
	private Environment env;

	@Test
	void findFloorTest1() {
		when(repo.findByFloorIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getFloorResponseDto(any(Long.class))));
		FloorResponseDto result = service.findFloor(0L);
		assertThat(result.getFloorId()).isEqualTo(0L);
	}

	@Test
	void findFloorTest2() {
		when(repo.findByFloorIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		FloorResponseDto result = service.findFloor(0L);
		assertThat(result).isNull();
	}

	@Test
	void findFloorsTest() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<FloorResponseDto> actual = entityGenerator.getFloorPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable)).thenReturn(actual);
		Page<FloorResponseDto> result = service.findFloors(dto);
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void saveFloorTest1() {
		FloorRequestDto req = entityGenerator.getFloorRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(123123123L);
		when(userService.findByPhone(any(Long.class))).thenReturn(user);
		when(repo.save(any(Floor.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveFloor(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveFloorTest2() {
		FloorRequestDto req = entityGenerator.getFloorRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(null);
		when(userService.newUser(any(String.class))).thenReturn(user);
		when(repo.save(any(Floor.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveFloor(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void updateFloorTest() {
		Floor floor = entityGenerator.getMockFloor();
		FloorRequestDto req = entityGenerator.getFloorRequestDto();
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(floor));
		ResponseEntity<CustomBaseResponseDto> result = service.updateFloor(any(Long.class), req);
		assertThat(result).isNotNull();
	}

	@Test
	void deleteFloorTest() {
		ResponseEntity<CustomBaseResponseDto> result = service.deleteFloor(any(Long.class));
		assertThat(result).isNotNull();
	}

}
