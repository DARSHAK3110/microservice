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
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.entity.Section;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.SectionRepository;
import com.training.library.repositories.ShelfRepository;

import jakarta.transaction.Transactional;

@Service
@PropertySource("classpath:message.properties")
public class ShelfService {
	@Autowired
	private ShelfRepository shelfRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private SectionRepository sectionRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	public ShelfResponseDto findShelf(Long id) {
		Optional<ShelfResponseDto> shelf = shelfRepository.findByShelfIdAndDeletedAtIsNull(id);
		if (shelf.isPresent()) {
			return shelf.get();
		}
		return null;
	}

	public Page<ShelfResponseDto> findShelfs(FilterDto dto) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return shelfRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageable);
	}

	public ResponseEntity<CustomBaseResponseDto> saveShelf(ShelfRequestDto dto, String userName) {
		User user = null;
		Shelf shelf = new Shelf();
		user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		shelf.setUser(user);
		Optional<Section> section = sectionRepository.findById(dto.getSectionId());
		if (section.isPresent()) {
			shelf.setSection(section.get());
		}

		shelf.setShelfNo(dto.getShelfNo());
		shelfRepository.save(shelf);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> updateShelf(Long id, ShelfRequestDto dto) {
		Optional<Shelf> shelfOptional = shelfRepository.findById(id);
		if (shelfOptional.isPresent()) {
			Shelf shelf = shelfOptional.get();
			Optional<Section> section = sectionRepository.findById(dto.getSectionId());
			if (section.isPresent()) {
				shelf.setSection(section.get());
			}
			shelf.setShelfNo(dto.getShelfNo());
			shelfRepository.save(shelf);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteShelf(Long id) {
		shelfRepository.deleteByShelfId(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public List<ShelfResponseDto> findShelfsBySection(Long sectionId) {
		return shelfRepository.getAllBySection_SectionIdAndDeletedAtIsNotNull(sectionId);
	}
}
