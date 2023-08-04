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

import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookStatus;
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

	public BookStatusResponseDto findBookStatus(Long id) {
		Optional<BookStatusResponseDto> bookStatus = bookStatusRepository.findByBookStatusIdAndDeletedAtIsNull(id);
		if (bookStatus.isPresent()) {
			return bookStatus.get();
		}
		return null;
	}

	public ResponseEntity<CustomBaseResponseDto> deleteBookStatus(Long id) {
		bookStatusRepository.deleteByBookStatusId(id);
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

}
