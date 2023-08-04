package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.entity.Location;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.LocationRepository;
import com.training.library.repositories.ShelfRepository;

import jakarta.transaction.Transactional;

@Service
@PropertySource("classpath:message.properties")
public class LocationService {

	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ShelfRepository shelfRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	public LocationResponseDto findLocation(Long id) {
		Optional<LocationResponseDto> location = locationRepository.findByLocationIdAndDeletedAtIsNull(id);
		if (location.isPresent()) {
			return location.get();
		}
		return null;
	}

	public Page<LocationResponseDto> findLocations(FilterDto dto) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return locationRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageable);
	}

	public ResponseEntity<CustomBaseResponseDto> saveLocation(LocationRequestDto dto, String userName) {
		Location location = new Location();
		User user = null;
		user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		location.setUser(user);
		location.setPosition(dto.getPosition());
		Optional<Shelf> shelf = shelfRepository.findById(dto.getShelfId());
		if (shelf.isPresent()) {
			location.setShelf(shelf.get());
		}
		location.setIsAvailable(true);
		locationRepository.save(location);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> updateLocation(Long id, LocationRequestDto dto) {
		Optional<Location> locationOptional = locationRepository.findById(id);
		if (locationOptional.isPresent()) {
			Location location = locationOptional.get();
			location.setPosition(dto.getPosition());
			Optional<Shelf> shelf = shelfRepository.findById(dto.getShelfId());
			if (shelf.isPresent()) {
				location.setShelf(shelf.get());
			}
			locationRepository.save(location);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteLocation(Long id) {
		locationRepository.deleteByLocationId(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));	
	}

	public Location findLocationById(Long id) {

		Optional<Location> location = locationRepository.findById(id);
		if (location.isPresent()) {
			return location.get();
		}
		return null;
	}

	public List<LocationResponseDto> findLocationsByShelf(Long shelfId) {
		return locationRepository.getAllByShelf_ShelfIdAndDeletedAtIsNotNull(shelfId);
	}

	public void updateLocationAvailability(Location location) {
		locationRepository.save(location);
	}
}
