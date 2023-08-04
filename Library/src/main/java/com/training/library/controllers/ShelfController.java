package com.training.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.services.ShelfService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/shelfs")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ShelfController {

	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private ShelfService shelfService;
	@Autowired
	private Environment env;

	@GetMapping("/shelf/{id}")
	public ResponseEntity<ShelfResponseDto> findShelf(@PathVariable Long id) {
		ShelfResponseDto shelf = shelfService.findShelf(id);
		return ResponseEntity.ok(shelf);
	}

	@GetMapping
	public ResponseEntity<Page<ShelfResponseDto>> findShelfs(FilterDto dto) {
		Page<ShelfResponseDto> shelfs = shelfService.findShelfs(dto);
		return ResponseEntity.ok(shelfs);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveShelf(@RequestBody ShelfRequestDto dto, HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return shelfService.saveShelf(dto, userName);
	}

	@PutMapping("/shelf/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateShelf(@PathVariable Long id, @RequestBody ShelfRequestDto dto) {
		return shelfService.updateShelf(id, dto);
	}

	@DeleteMapping("/shelf/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteShelf(@PathVariable Long id) {
		return shelfService.deleteShelf(id);
	}
	@GetMapping("/sections/{sectionId}")
	public ResponseEntity<List<ShelfResponseDto>> findShelfsBySectionId(@PathVariable("sectionId") Long sectionId) {
		List<ShelfResponseDto> shelfs = shelfService.findShelfsBySection(sectionId);
		return ResponseEntity.ok(shelfs);
	}

}
