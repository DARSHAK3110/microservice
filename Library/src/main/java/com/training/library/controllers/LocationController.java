package com.training.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.services.LocationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/library/api/v1/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping("/{id}")
	public ResponseEntity<LocationResponseDto> findLocation(@PathVariable Long id) {
		LocationResponseDto location = locationService.findLocation(id);
		return ResponseEntity.ok(location);
	}

	@GetMapping
	public ResponseEntity<List<LocationResponseDto>> findLocations() {
		List<LocationResponseDto> locations = locationService.findLocations();
		return ResponseEntity.ok(locations);
	}

	@PostMapping
	public ResponseEntity<List<LocationResponseDto>> saveLocation(@RequestBody LocationRequestDto dto,
			HttpServletRequest req) {
		String userName = req.getHeader("username");
		List<LocationResponseDto> locations = locationService.saveLocation(dto, userName);
		return ResponseEntity.ok(locations);
	}

	@PutMapping("/{id}")
	public ResponseEntity<List<LocationResponseDto>> updateLocation(@PathVariable Long id,
			@RequestBody LocationRequestDto dto) {
		List<LocationResponseDto> locations = locationService.updateLocation(id, dto);
		return ResponseEntity.ok(locations);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<List<LocationResponseDto>> deleteLocation(@PathVariable Long id) {
		List<LocationResponseDto> locations = locationService.deleteLocation(id);
		return ResponseEntity.ok(locations);
	}

}
