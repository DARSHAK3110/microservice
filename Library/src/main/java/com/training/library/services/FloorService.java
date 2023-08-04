package com.training.library.services;

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
import com.training.library.dto.request.FloorRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.FloorResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.User;
import com.training.library.repositories.FloorRepository;

@Service
@PropertySource("classpath:message.properties")
public class FloorService {
	@Autowired
	private UserService userService;
	@Autowired
	private FloorRepository floorRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	public FloorResponseDto findFloor(Long id) {
		Optional<FloorResponseDto> floor = floorRepository.findByFloorIdAndDeletedAtIsNull(id);
		if (floor.isPresent()) {
			return floor.get();
		}
		return null;
	}

	public Page<FloorResponseDto> findFloors(FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return floorRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageble);
	}

	public ResponseEntity<CustomBaseResponseDto> saveFloor(FloorRequestDto dto, String userName) {
		User user = null;
		Floor floor = new Floor();
		user= userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		floor.setUser(user);
		floor.setFloorNo(dto.getFloorNo());
		floorRepository.save(floor);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> updateFloor(Long id, FloorRequestDto dto) {
		Optional<Floor> floorOptional = floorRepository.findById(id);
		if (floorOptional.isPresent()) {
			Floor floor = floorOptional.get();
			floor.setFloorNo(dto.getFloorNo());
			floorRepository.save(floor);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> deleteFloor(Long id) {
		floorRepository.deleteByFloorId(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

}
