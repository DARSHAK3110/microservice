package com.training.library.services;

import java.util.List;
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
import com.training.library.dto.request.SectionRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.Section;
import com.training.library.entity.User;
import com.training.library.repositories.FloorRepository;
import com.training.library.repositories.SectionRepository;

import jakarta.transaction.Transactional;

@Service
@PropertySource("classpath:message.properties")
public class SectionService {
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private FloorRepository floorRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	public SectionResponseDto findSection(Long id) {
		Optional<SectionResponseDto> section = sectionRepository.findBySectionIdAndDeletedAtIsNull(id);
		if (section.isPresent()) {
			return section.get();
		}
		return null;
	}

	public Page<SectionResponseDto> findSections(FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return sectionRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageble);
	}

	public ResponseEntity<CustomBaseResponseDto> saveSection(SectionRequestDto dto, String userName) {
		Section section = new Section();
		User user = null;
		user = userService.findByPhone(Long.parseLong(userName));
		
		if (user == null) {
			user = userService.newUser(userName);
		}
		section.setUser(user);
		Optional<Floor> floor = floorRepository.findByFloorNo(dto.getFloorNo());
		if (floor.isPresent()) {
			section.setFloor(floor.get());
		}
		section.setSectionName(dto.getSectionName());
		sectionRepository.save(section);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> updateSection(Long id, SectionRequestDto dto) {
		Optional<Section> sectionOptional = sectionRepository.findById(id);
		if (sectionOptional.isPresent()) {
			Section section = sectionOptional.get();
			Optional<Floor> floor = floorRepository.findByFloorNo(dto.getFloorNo());
			if (floor.isPresent()) {
				section.setFloor(floor.get());
			}
			section.setSectionName(dto.getSectionName());
			sectionRepository.save(section);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteSection(Long id) {
		sectionRepository.deleteById(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public List<SectionResponseDto> findSectionsByFloors(Long floorId) {
		return sectionRepository.getAllByFloor_FloorNoAndDeletedAtIsNotNull(floorId);
	}
}
