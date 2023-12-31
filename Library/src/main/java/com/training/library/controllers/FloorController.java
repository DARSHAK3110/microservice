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
import com.training.library.dto.request.FloorRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.FloorResponseDto;
import com.training.library.services.FloorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@PropertySource("classpath:message.properties")
@RequestMapping("/library/api/v1/floors")
public class FloorController {
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	@Autowired
	private FloorService floorService;

	@GetMapping("/floor/{id}")
	public ResponseEntity<FloorResponseDto> findFloor(@PathVariable Long id) {
		FloorResponseDto floor = floorService.findFloor(id);
		return ResponseEntity.ok(floor);
	}

	@GetMapping
	public ResponseEntity<Page<FloorResponseDto>> findFloors(FilterDto dto)
	{
		Page<FloorResponseDto> floors = floorService.findFloors(dto);
		return ResponseEntity.ok(floors);
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveFloor(HttpServletRequest req,
			@Valid @RequestBody FloorRequestDto dto) {
		String userName = req.getHeader("username");
		floorService.saveFloor(dto, userName);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@PutMapping("/floor/{id}")
	public ResponseEntity<CustomBaseResponseDto> updateFloor(@PathVariable Long id, @RequestBody FloorRequestDto dto) {
		floorService.updateFloor(id, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@DeleteMapping("/floor/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteFloor(@PathVariable Long id) {
		floorService.deleteFloor(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
}
