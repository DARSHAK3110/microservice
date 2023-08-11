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
import org.springframework.transaction.annotation.Transactional;

import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Location;
import com.training.library.repositories.BookStatusRepository;

@Service
@PropertySource("classpath:message.properties")
public class BookStatusService {
	@Autowired
	private BookStatusRepository bookStatusRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	@Autowired
	private LocationService locationService;
	@Autowired
	private BookDetailsService bookDetailsService;

	public BookStatusResponseDto findBookStatus(Long id) {
		Optional<BookStatusResponseDto> bookStatus = bookStatusRepository.findByBookStatusIdAndDeletedAtIsNull(id);
		if (bookStatus.isPresent()) {
			return bookStatus.get();
		}
		return null;
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteBookStatus(Long id) {
		BookStatus bookStatus = findBookById(id);
		if (Boolean.TRUE.equals(bookStatus.isAvailable())) {
			Location locationOld = locationService.findLocationById(bookStatus.getLocation().getLocationId());
			locationOld.setIsAvailable(true);
			locationService.updateLocationAvailability(locationOld);
			bookDetailsService.setAvailableCopies(bookStatus.getBookDetails(), "remove");
			bookStatusRepository.deleteByBookStatusId(id);
		} else {
			throw new RuntimeException("For delete book, book really required at location");
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}
	
	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteBookStatus(BookStatus bookStatus) {
		if (Boolean.TRUE.equals(bookStatus.isAvailable())) {
			Location locationOld = locationService.findLocationById(bookStatus.getLocation().getLocationId());
			locationOld.setIsAvailable(true);
			locationService.updateLocationAvailability(locationOld);
			bookDetailsService.setAvailableCopies(bookStatus.getBookDetails(), "remove");
			bookStatusRepository.deleteByBookStatusId(bookStatus.getBookStatusId());
		} else {
			throw new RuntimeException("For delete book, book really required at location");
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public ResponseEntity<CustomBaseResponseDto> updateBookStatus(Long id, BookStatusRequestDto dto) {
		Optional<BookStatus> bookStatus = bookStatusRepository.findById(dto.getBookStatusId());
		if (bookStatus.isPresent()) {
			BookStatus bs = bookStatus.get();
			bs.setAvailable(dto.getIsAvailable());
			bs.setLocation(locationService.findLocationById(dto.getLocationId()));
			bookStatusRepository.save(bs);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public BookStatus findBookById(Long bookStatusId) {
		Optional<BookStatus> bookStatus = bookStatusRepository.findById(bookStatusId);
		if (bookStatus.isPresent()) {
			return bookStatus.get();
		}
		return null;
	}

	public Page<BookStatusResponseDto> findAllBookStatusByBookDetailsId(FilterDto dto, Long id) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		if (dto.getAvailability() == 0) {
			return bookStatusRepository.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(pageble, id);
		} else {
			if (dto.getAvailability() == 1) {
				return bookStatusRepository.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(pageble, id, true);
			} else {
				return bookStatusRepository.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(pageble, id, false);
			}
		}
	}

	public void updateBookStatusAvailability(BookStatus bookStatus) {
		bookStatusRepository.save(bookStatus);
	}

	public BookStatus findByLocationId(Long locationId) {
		Optional<BookStatus> bookStatusOptional = bookStatusRepository.findByLocation_LocationId(locationId);
		if (bookStatusOptional.isPresent()) {
			return bookStatusOptional.get();
		}
		return null;
	}

	public ResponseEntity<CustomBaseResponseDto> getCountByLocation(Long id) {
		Long counter = bookStatusRepository.countByLocation_LocationIdAndDeletedAtIsNull(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(String.valueOf(counter)));
	}

	public ResponseEntity<CustomBaseResponseDto> getCountByShelf(Long id) {
		Long counter = bookStatusRepository.countByLocation_Shelf_ShelfIdAndDeletedAtIsNull(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(String.valueOf(counter)));
	}

	public ResponseEntity<CustomBaseResponseDto> getCountBySection(Long id) {
		Long counter = bookStatusRepository.countByLocation_Shelf_Section_SectionIdAndDeletedAtIsNull(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(String.valueOf(counter)));
	}

	public ResponseEntity<CustomBaseResponseDto> getCountByFloor(Long id) {
		Long counter = bookStatusRepository.countByLocation_Shelf_Section_Floor_FloorIdAndDeletedAtIsNull(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(String.valueOf(counter)));
	}

	public Page<BookStatusResponseDto> findAllBooksByLocation(Long id, FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return bookStatusRepository.findAllByLocationId(id, pageble);
	}

	public Page<BookStatusResponseDto> findAllBooksByShelf(Long id, FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return bookStatusRepository.findAllByShelfId(id, pageble);
	}

	public Page<BookStatusResponseDto> findAllBooksByFloor(Long id, FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return bookStatusRepository.findAllByFloorId(id, pageble);
	}

	public Page<BookStatusResponseDto> findAllBooksBySection(Long id, FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return bookStatusRepository.findAllBySectionId(id, pageble);
	}

}
