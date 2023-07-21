package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.FloorRequestDto;
import com.training.library.dto.response.FloorResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.User;
import com.training.library.repositories.FloorRepository;
import com.training.library.repositories.UserRepository;

@Service
public class FloorService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FloorRepository floorRepository;

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

	public void saveFloor(FloorRequestDto dto, String userName) {
		Floor floor = new Floor();
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong(userName));
		User user = null;
		if (userOptional.isEmpty()) {
			User newUser = new User();
			newUser.setPhone(Long.parseLong(userName));
			user = userRepository.save(newUser);
		} else {
			user = userOptional.get();
		}
		floor.setUser(user);
		floor.setFloorNo(dto.getFloorNo());
		floorRepository.save(floor);
	}

	public void updateFloor(Long id, FloorRequestDto dto) {
		Optional<Floor> floorOptional = floorRepository.findById(id);
		if (floorOptional.isPresent()) {
			Floor floor = floorOptional.get();
			floor.setFloorNo(dto.getFloorNo());
			floorRepository.save(floor);
		}

	}

	public void deleteFloor(Long id) {
		floorRepository.deleteByFloorId(id);
	}

}
