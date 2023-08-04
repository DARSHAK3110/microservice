package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import com.training.library.dto.request.SectionRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.entity.Section;
import com.training.library.entity.User;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.FloorRepository;
import com.training.library.repositories.SectionRepository;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SectionServiceTest {

	@Mock
	private SectionRepository repo;
	@Mock
	private Environment env;
	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private SectionService service;
	@Autowired
	private EntityGenerator entityGenerator;
	@Mock
	private FloorRepository floorRepository;
	@Mock
	private UserService userService;

	@Test
	void findSectionTest1() {
		when(repo.findBySectionIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getSectionResponseDto(0L)));
		SectionResponseDto result = service.findSection(0L);
		assertThat(result.getSectionId()).isEqualTo(0L);
	}

	@Test
	void findSectionTest2() {
		when(repo.findBySectionIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		SectionResponseDto result = service.findSection(any(Long.class));
		assertThat(result).isNull();
	}

	@Test
	void testFindSections() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<SectionResponseDto> actual = entityGenerator.getSectionPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable)).thenReturn(actual);
		Page<SectionResponseDto> result = service.findSections(dto);
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void saveSectionTest2() {
		SectionRequestDto req = entityGenerator.getSectionRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(null);
		when(floorRepository.findByFloorNo(any(Long.class))).thenReturn(Optional.empty());
		when(userService.newUser("1231231231")).thenReturn(user);
		when(repo.save(any(Section.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveSection(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveSectionTest3() {
		SectionRequestDto req = entityGenerator.getSectionRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(user);
		when(floorRepository.findByFloorNo(any(Long.class))).thenReturn(Optional.empty());
		when(repo.save(any(Section.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveSection(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveSectionTest1() {
		SectionRequestDto req = entityGenerator.getSectionRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(user);
		when(floorRepository.findByFloorNo(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockFloor()));
		when(repo.save(any(Section.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveSection(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveSectionTest4() {
		SectionRequestDto req = entityGenerator.getSectionRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(user);
		when(floorRepository.findByFloorNo(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockFloor()));
		when(repo.save(any(Section.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveSection(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void updateSectionTest1() {
		Section section = entityGenerator.getMockSection();
		SectionRequestDto req = entityGenerator.getSectionRequestDto();
		when(floorRepository.findByFloorNo(any(Long.class))).thenReturn(Optional.empty());
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(section));
		ResponseEntity<CustomBaseResponseDto> result = service.updateSection(0L, req);
		assertThat(result).isNotNull();
	}

	@Test
	void updateSectionTest2() {
		Section section = entityGenerator.getMockSection();
		SectionRequestDto req = entityGenerator.getSectionRequestDto();
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(section));
		when(floorRepository.findByFloorNo(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockFloor()));
		ResponseEntity<CustomBaseResponseDto> result = service.updateSection(0L, req);
		assertThat(result).isNotNull();
	}

	@Test
	void deleteSectionTest() {
		ResponseEntity<CustomBaseResponseDto> result = service.deleteSection(0L);
		assertThat(result).isNotNull();
	}

	@Test
	void findSectionsByFloorsTest1() {
		SectionResponseDto dto = entityGenerator.getSectionResponseDto(any(Long.class));
		when(repo.getAllByFloor_FloorNoAndDeletedAtIsNotNull(0L)).thenReturn(List.of(dto));
		List<SectionResponseDto> result = service.findSectionsByFloors(0L);
		assertThat(result.size()).isPositive();
	}

	@Test
	void findSectionsByFloorsTest2() {
		List<SectionResponseDto> list = new ArrayList<>();
		when(repo.getAllByFloor_FloorNoAndDeletedAtIsNotNull(any(Long.class))).thenReturn(null);
		List<SectionResponseDto> result = service.findSectionsByFloors(0L);
		assertThat(result).isNull();
	}

}
