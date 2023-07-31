package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.entity.Location;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.LocationRepository;
import com.training.library.repositories.ShelfRepository;
import com.training.library.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class LocationService {

	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ShelfRepository shelfRepository;

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

	public void saveLocation(LocationRequestDto dto, String userName) {
		Location location = new Location();
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong("9725953035"));
		User user = null;
		if (userOptional.isEmpty()) {
			User newUser = new User();
			newUser.setPhone(Long.parseLong("9725953035"));
			user = userRepository.save(newUser);
		} else {
			user = userOptional.get();
		}
		location.setUser(user);
		location.setPosition(dto.getPosition());
		Optional<Shelf> shelf = shelfRepository.findById(dto.getShelfId());
		if (shelf.isPresent()) {
			location.setShelf(shelf.get());
		}
		location.setIsAvailable(true);
		locationRepository.save(location);

	}

	public void updateLocation(Long id, LocationRequestDto dto) {
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

	}

	@Transactional
	public void deleteLocation(Long id) {
		locationRepository.deleteByLocationId(id);
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
