package com.training.library.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public List<LocationResponseDto> findLocations() {
		Optional<List<LocationResponseDto>> locations = locationRepository.findAllByDeletedAtIsNull();
		if (locations.isPresent()) {
			return locations.get();
		}
		return Collections.emptyList();
	}

	public List<LocationResponseDto> saveLocation(LocationRequestDto dto, String userName) {
		Location location = new Location();
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong(userName));
		User user = null;
		if (userOptional.isEmpty()) {
			User newUser = new User();
			newUser.setPhone(Long.parseLong(userName));
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
		locationRepository.save(location);
		return findLocations();
	}

	public List<LocationResponseDto> updateLocation(Long id, LocationRequestDto dto) {
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
		return findLocations();
	}

	@Transactional	
	public List<LocationResponseDto> deleteLocation(Long id) {
		locationRepository.deleteByLocationId(id);
		return findLocations();
	}

	public Location findLocationById(Long id) {
		
		Optional<Location> location = locationRepository.findById(id);
		if (location.isPresent()) {
			return location.get();
		}
		return null;
	}
}
