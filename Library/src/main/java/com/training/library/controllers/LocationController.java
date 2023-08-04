package com.training.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.services.LocationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/locations")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class LocationController {

	@Autowired
	private LocationService locationService;


	@GetMapping("/location/{id}")
	public ResponseEntity<LocationResponseDto> findLocation(@PathVariable Long id) {
		LocationResponseDto location = locationService.findLocation(id);
		return ResponseEntity.ok(location);
	}

	@GetMapping
	public ResponseEntity<Page<LocationResponseDto>> findLocations(FilterDto dto) {
		Page<LocationResponseDto> locations = locationService.findLocations(dto);
		return ResponseEntity.ok(locations);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveLocation(@RequestBody LocationRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return locationService.saveLocation(dto, userName);
	}

	@PutMapping("/location/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateLocation(@PathVariable Long id,
			@RequestBody LocationRequestDto dto) {
		return locationService.updateLocation(id, dto);
	}

	@DeleteMapping("/location/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteLocation(@PathVariable Long id) {
		return locationService.deleteLocation(id);
	}
	@GetMapping("/shelfs/{shelfId}")
	public ResponseEntity<List<LocationResponseDto>> findLocationsByShelfId(@PathVariable("shelfId") Long shelfId) {
		List<LocationResponseDto> locations = locationService.findLocationsByShelf(shelfId);
		return ResponseEntity.ok(locations);
	}

}
