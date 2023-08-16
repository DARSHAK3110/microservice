package com.training.library.services;

import java.util.Date;
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

import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookReservation;
import com.training.library.entity.User;
import com.training.library.repositories.BookReservationRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@PropertySource("classpath:message.properties")
public class BookReservationService {

	@Autowired
	private BookReservationRepository bookReservationRepository;
	@Autowired
	private BookDetailsService bookDetailsService;
	@Autowired
	private UserService userService;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	public BookReservationResponseDto findBookReservation(Long id) {
		Optional<BookReservationResponseDto> bookReservation = bookReservationRepository
				.findByBookReservationIdAndDeletedAtIsNull(id);
		if (bookReservation.isPresent()) {
			return bookReservation.get();
		}
		return null;
	}

	public Page<BookReservationResponseDto> findAllBookReservation(FilterDto dto, String userId) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		if(dto.isUser()) {
			return bookReservationRepository.findAllByDeletedAtIsNull(userId, pageable,Long.parseLong(userId));
		}
		
		Page<BookReservationResponseDto> result = bookReservationRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageable);
		List<BookReservationResponseDto> content = result.getContent();
		for (BookReservationResponseDto response : content) {
		response.setTotalRequest(getTotalReservations(response.getBookId()));
		response.setAccptedRequest(getTotalAcceptedReservations(response.getBookId()));
		}
		return result;
	}

	public ResponseEntity<CustomBaseResponseDto> saveBookReservation(BookReservationRequestDto dto, String userName) {
		User reserver = userService.findByPhone(dto.getPhone());
		BookDetails bookDetails = bookDetailsService.findBookDetailsById(dto.getBookDetailsId());
		BookReservation br = new BookReservation();
		br.setReserver(reserver);
		br.setBookDetails(bookDetails);

		User user = userService.findByPhone(Long.parseLong(userName));

		if (user == null) {
			user = userService.newUser(userName);
		}
		br.setUser(user);
		br.setReservationDate(dto.getReservationDate());
		bookReservationRepository.save(br);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> deleteBookReservation(Long id) {
		bookReservationRepository.deleteByBookReservationId(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public boolean checkReservation(Long bookDetailsId, String userName) {

		Optional<BookReservation> bookReservation = bookReservationRepository
				.findByBookDetails_BookDetailsIdAndReserver_PhoneAndDeletedAtIsNull(bookDetailsId,
						Long.parseLong(userName));
		if (bookReservation.isPresent()) {
			return true;
		}
		return false;
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> saveBookReservationStatus(Long id, @Valid Boolean status) {
		Optional<BookReservation> reservationOptional = bookReservationRepository.findById(id);
		if (reservationOptional.isPresent()) {
			BookReservation reservation = reservationOptional.get();
			reservation.setIsAccepted(status);
			bookReservationRepository.save(reservation);
		}
		if (!status) {
			deleteBookReservation(id);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public Long getTotalReservations(Long bookDetailsId) {
		return bookReservationRepository.countByBookDetails_BookDetailsIdAndDeletedAtIsNull(bookDetailsId);
	}

	public Long getTotalAcceptedReservations(Long bookDetailsId) {
		return bookReservationRepository
				.countByBookDetails_BookDetailsIdAndDeletedAtIsNullAndIsAcceptedTrue(bookDetailsId);
	}

	@Transactional
	public void deleteByReservationDate() {
		Date expireDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
		bookReservationRepository.deleteAllByReservationDateBefore(expireDate);

	}
}
