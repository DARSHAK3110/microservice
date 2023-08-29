package com.training.library.controllers;

import java.io.IOException;
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

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.dto.response.MessageResponseDto;
import com.training.library.services.LocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
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

@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveLocation(@Valid @RequestBody LocationRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return locationService.saveLocation(dto, userName);
	}

@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/location/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateLocation(@PathVariable Long id,
			@Valid @RequestBody LocationRequestDto dto) {
		return locationService.updateLocation(id, dto);
	}

@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/location/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteLocation(@PathVariable Long id) {
		return locationService.deleteLocation(id);
	}

	@PutMapping("/auto")
	public ResponseEntity<CustomBaseResponseDto> autoDelete(@Valid @RequestBody MessageResponseDto dto,
			HttpServletRequest req)throws StreamReadException, DatabindException, IOException {
		LocationRequestDto locDto = new ObjectMapper().convertValue(dto.getEntityRequestDto(), LocationRequestDto.class);
		return locationService.deleteLetter(locDto, dto.getUserName());
	}

	@GetMapping("/shelfs/{shelfId}")
	public ResponseEntity<List<LocationResponseDto>> findLocationsByShelfId(@PathVariable("shelfId") Long shelfId) {
		List<LocationResponseDto> locations = locationService.findLocationsByShelf(shelfId);
		return ResponseEntity.ok(locations);
	}
	@PutMapping("/autodelete")
	public ResponseEntity<CustomBaseResponseDto> autoDelete(@Valid @RequestBody LocationRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return locationService.autoDeleteMessage(dto, userName,"location");
	}
	
}
