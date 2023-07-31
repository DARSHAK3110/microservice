package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.SectionRequestDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.Section;
import com.training.library.entity.User;
import com.training.library.repositories.FloorRepository;
import com.training.library.repositories.SectionRepository;
import com.training.library.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class SectionService {
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FloorRepository floorRepository;

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

	public void saveSection(SectionRequestDto dto, String userName) {
		Section section = new Section();
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong("9725953035"));
		User user = null;
		if (userOptional.isEmpty()) {
			User newUser = new User();
			newUser.setPhone(Long.parseLong(userName));
			user = userRepository.save(newUser);
		} else {
			user = userOptional.get();
		}
		section.setUser(user);
		Optional<Floor> floor = floorRepository.findByFloorNo(dto.getFloorNo());
		if (floor.isPresent()) {
			section.setFloor(floor.get());
		}
		section.setSectionName(dto.getSectionName());
		sectionRepository.save(section);

	}

	public void updateSection(Long id, SectionRequestDto dto) {
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
	}

	@Transactional
	public void deleteSection(Long id) {
		sectionRepository.deleteBySectionId(id);
	}

	public List<SectionResponseDto> findSectionsByFloors(Long floorId) {
		
		return sectionRepository.getAllByFloor_FloorNoAndDeletedAtIsNotNull(floorId);
	}
}
