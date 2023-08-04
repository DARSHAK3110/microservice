package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.entity.Location;
import com.training.library.entity.User;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.LocationRepository;
import com.training.library.repositories.ShelfRepository;
import com.training.library.repositories.UserRepository;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LocationServiceTest {

	@Mock
	private ShelfRepository shelfRepository;

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private LocationService service;
	@Mock
	private LocationRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;

	@Mock
	private UserService userService;
	@Mock
	private Environment env;

	@Test
	void findLocationTest1() {
		when(repo.findByLocationIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getLocationResponseDto(0L)));
		LocationResponseDto result = service.findLocation(0L);
		assertThat(result.getShelfId()).isEqualTo(0L);
	}

	@Test
	void findLocationTest2() {
		when(repo.findByLocationIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		LocationResponseDto result = service.findLocation(0L);
		assertThat(result).isNull();
	}
	
	@Test
	void findLocationsTest() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<LocationResponseDto> actual = entityGenerator.getLocationPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable)).thenReturn(actual);
		Page<LocationResponseDto> result = service.findLocations(dto);
		assertThat(result).isEqualTo(actual);
	
	}
	@Test
	void saveLocationTest1() {
		LocationRequestDto req = entityGenerator.getLocationRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(shelfRepository.findById(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockShelf()));
		when(repo.save(any(Location.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveLocation(req, "1231231231");
		assertThat(result).isNotNull();
	}
	
	
	@Test
	void saveLocationTest2() {
		LocationRequestDto req = entityGenerator.getLocationRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(shelfRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(repo.save(any(Location.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveLocation(req, "1231231231");
		assertThat(result).isNotNull();
	}
	@Test
	void saveLocationTest3() {
		LocationRequestDto req = entityGenerator.getLocationRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(1231231231L)).thenReturn(null);
		when(shelfRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(userService.newUser("1231231231")).thenReturn(user);
		when(repo.save(any(Location.class))).thenReturn(null);
		ResponseEntity<CustomBaseResponseDto> result = service.saveLocation(req, "1231231231");
		assertThat(result).isNotNull();
	}
	@Test
	void updateLocationTest1() {
		Location location = entityGenerator.getMockLocation();
		LocationRequestDto req = entityGenerator.getLocationRequestDto();
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(location));
		ResponseEntity<CustomBaseResponseDto> result = service.updateLocation(0L, req);
		assertThat(result).isNotNull();
	}
	
	@Test
	void updateShelfTest2() {
		Location location = entityGenerator.getMockLocation();
		LocationRequestDto req = entityGenerator.getLocationRequestDto();
		when(shelfRepository.findById(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockShelf()));
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(location));
		ResponseEntity<CustomBaseResponseDto> result = service.updateLocation(0L, req);
		assertThat(result).isNotNull();
	}

	@Test
	void testDeleteShelf() {
		ResponseEntity<CustomBaseResponseDto> result = service.deleteLocation(0L);
		assertThat(result).isNotNull();
	}
	@Test
	void findLocationByIdTest1() {
		Optional<Location> location = Optional.of(entityGenerator.getMockLocation());
		location.get().setLocationId(0L);
		when(repo.findById(0L)).thenReturn(location);
		Location result = service.findLocationById(0L);
		assertThat(result.getLocationId()).isEqualTo(0L);
	}

	@Test
	void findLocationByIdTest2() {
		when(repo.findById(0L)).thenReturn(Optional.empty());
		Location result = service.findLocationById(0L);
		assertThat(result).isNull();
	}

	@Test
	void findLocationsByShelfTest1() {
		LocationResponseDto dto = entityGenerator.getLocationResponseDto(any(Long.class));
		when(repo.getAllByShelf_ShelfIdAndDeletedAtIsNotNull(0L)).thenReturn(List.of(dto));
		List<LocationResponseDto> result = service.findLocationsByShelf(0L);
		assertThat(result.size()).isPositive();
	}

	@Test
	void findLocationsByShelfTest2() {
		when(repo.getAllByShelf_ShelfIdAndDeletedAtIsNotNull(any(Long.class))).thenReturn(null);
		List<LocationResponseDto> result = service.findLocationsByShelf(0L);
		assertThat(result).isNull();
	}

	@Test
	void testUpdateLocationAvailability() {
		Location location = entityGenerator.getMockLocation();
		when(repo.save(any(Location.class))).thenReturn(location);
		service.updateLocationAvailability(location);
		verify(repo, times(1)).save(location);
		
	}

}
