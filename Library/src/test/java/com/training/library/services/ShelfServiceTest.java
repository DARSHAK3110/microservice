package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.SectionRepository;
import com.training.library.repositories.ShelfRepository;
import com.training.library.repositories.UserRepository;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ShelfServiceTest {


	@Mock
	private ShelfRepository repo;

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private ShelfService service;
	@Autowired
	private EntityGenerator entityGenerator;

	@Mock
	private SectionRepository sectionRepository;
	@Mock
	private UserService userService;
	@Mock
	private Environment env;
	@Test
	void findShelfTest1() {
		when(repo.findByShelfIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getShelfResponseDto(0L)));
		ShelfResponseDto result = service.findShelf(0L);
		assertThat(result.getShelfId()).isEqualTo(0L);
	}

	@Test
	void findShelfTest2() {
		when(repo.findByShelfIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		ShelfResponseDto result = service.findShelf(any(Long.class));
		assertThat(result).isNull();
	}
	
	@Test
	void findShelfsTest() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<ShelfResponseDto> actual = entityGenerator.getShelfPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable)).thenReturn(actual);
		Page<ShelfResponseDto> result = service.findShelfs(dto);
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void saveShelfTest1() {
		ShelfRequestDto req = entityGenerator.getShelfRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(user);
		when(sectionRepository.findById(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockSection()));
		when(repo.save(any(Shelf.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveShelf(req, "1231231231");
		assertThat(result).isNotNull();
	}
	
	
	@Test
	void saveShelfTest2() {
		ShelfRequestDto req = entityGenerator.getShelfRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(user);
		when(sectionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(repo.save(any(Shelf.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveShelf(req, "1231231231");
		assertThat(result).isNotNull();
	}
	@Test
	void saveShelfTest3() {
		ShelfRequestDto req = entityGenerator.getShelfRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(null);
		when(sectionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(userService.newUser("1231231231")).thenReturn(user);
		when(repo.save(any(Shelf.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveShelf(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void updateShelfTest1() {
		Shelf shelf = entityGenerator.getMockShelf();
		ShelfRequestDto req = entityGenerator.getShelfRequestDto();
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(shelf));
		ResponseEntity<CustomBaseResponseDto> result = service.updateShelf(0L, req);
		assertThat(result).isNotNull();
	}
	
	@Test
	void updateShelfTest2() {
		Shelf shelf = entityGenerator.getMockShelf();
		ShelfRequestDto req = entityGenerator.getShelfRequestDto();
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(shelf));
		when(sectionRepository.findById(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockSection()));
		ResponseEntity<CustomBaseResponseDto> result = service.updateShelf(0L, req);
		assertThat(result).isNotNull();
	}


	@Test
	void testDeleteShelf() {
		ResponseEntity<CustomBaseResponseDto> result = service.deleteShelf(0L);
		assertThat(result).isNotNull();
	}
	
	@Test
	void findShelfsBySectionTest1() {
		ShelfResponseDto dto = entityGenerator.getShelfResponseDto(any(Long.class));
		when(repo.getAllBySection_SectionIdAndDeletedAtIsNotNull(0L)).thenReturn(List.of(dto));
		List<ShelfResponseDto> result = service.findShelfsBySection(0L);
		assertThat(result.size()).isPositive();
	}

	@Test
	void findShelfsBySectionTest2() {
		when(repo.getAllBySection_SectionIdAndDeletedAtIsNotNull(any(Long.class))).thenReturn(null);
		List<ShelfResponseDto> result = service.findShelfsBySection(0L);
		assertThat(result).isNull();
	}
}
