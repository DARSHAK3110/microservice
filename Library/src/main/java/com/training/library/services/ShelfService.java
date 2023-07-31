package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.entity.Section;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.SectionRepository;
import com.training.library.repositories.ShelfRepository;
import com.training.library.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ShelfService {
	@Autowired
	private ShelfRepository shelfRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SectionRepository sectionRepository;

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

	public void saveShelf(ShelfRequestDto dto, String userName) {
		Shelf shelf = new Shelf();
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong("9725953035"));
		User user = null;
		if (userOptional.isEmpty()) {
			User newUser = new User();
			newUser.setPhone(Long.parseLong(userName));
			user = userRepository.save(newUser);
		} else {
			user = userOptional.get();
		}
		shelf.setUser(user);
		Optional<Section> section = sectionRepository.findById(dto.getSectionId());
		if (section.isPresent()) {
			shelf.setSection(section.get());
		}

		shelf.setShelfNo(dto.getShelfNo());
		shelfRepository.save(shelf);
	}

	public void updateShelf(Long id, ShelfRequestDto dto) {
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
	}

	@Transactional
	public void deleteShelf(Long id) {
		shelfRepository.deleteByShelfId(id);
	}

	public List<ShelfResponseDto> findShelfsBySection(Long sectionId) {
		return shelfRepository.getAllBySection_SectionIdAndDeletedAtIsNotNull(sectionId);
	}
}
