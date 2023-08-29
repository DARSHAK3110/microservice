package com.training.library.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
import com.training.library.dto.request.SectionRequestDto;
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.MessageResponseDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.services.SectionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@CrossOrigin
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/sections")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SectionController {

	@Autowired
	private SectionService sectionService;

	@GetMapping("/floors/{floorNo}")
	public ResponseEntity<List<SectionResponseDto>> findSectionsByFloor(@PathVariable Long floorNo) {
		List<SectionResponseDto> sections = sectionService.findSectionsByFloors(floorNo);
		return ResponseEntity.ok(sections);
	}

	@GetMapping("/section/{id}")
	public ResponseEntity<SectionResponseDto> findSection(@PathVariable Long id) {
		SectionResponseDto section = sectionService.findSection(id);
		return ResponseEntity.ok(section);
	}

	@GetMapping
	public ResponseEntity<Page<SectionResponseDto>> findSections(FilterDto dto) {
		Page<SectionResponseDto> sections = sectionService.findSections(dto);
		return ResponseEntity.ok(sections);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveSection(@Valid @RequestBody SectionRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return sectionService.saveSection(dto, userName);
	}

	@PutMapping("/section/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateSection(@PathVariable Long id,
			@RequestBody SectionRequestDto dto) {
		return sectionService.updateSection(id, dto);
	}

	@DeleteMapping("/section/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteSection(@PathVariable Long id) {
		return sectionService.deleteSection(id);
	}
	
	@PutMapping("/auto")
	public ResponseEntity<CustomBaseResponseDto> autoDelete(@Valid @RequestBody MessageResponseDto dto,
			HttpServletRequest req)throws StreamReadException, DatabindException, IOException {
		SectionRequestDto sectionDto = new ObjectMapper().convertValue(dto.getEntityRequestDto(), SectionRequestDto.class);
		return sectionService.deleteLetter(sectionDto, dto.getUserName());
	}
	
	@PutMapping("/autodelete")
	public ResponseEntity<CustomBaseResponseDto> autoDelete(@Valid @RequestBody SectionRequestDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return sectionService.autoDeleteMessage(dto, userName,"section");
	}
}
