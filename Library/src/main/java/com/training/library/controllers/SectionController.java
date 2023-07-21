package com.training.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.SectionRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.services.SectionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/sections")
public class SectionController {

	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	@Autowired
	private SectionService sectionService;

	@GetMapping("/floors/{floorNo}")
	public ResponseEntity<List<SectionResponseDto>> findSectionsByFloor(@PathVariable("floorNo") Long floorNo) {
		System.out.println("aavyu");
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
		String userName = req.getHeader("username");
		sectionService.saveSection(dto, userName);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@PutMapping("/section/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateSection(@PathVariable Long id,
			@RequestBody SectionRequestDto dto) {
		sectionService.updateSection(id, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@DeleteMapping("/section/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteSection(@PathVariable Long id) {
		sectionService.deleteSection(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
}
