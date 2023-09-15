package com.training.library.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.training.library.dto.request.EmailRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.emails.EmailSender;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookReservation;
import com.training.library.entity.BookStatus;
import com.training.library.entity.User;
import com.training.library.repositories.BookReservationRepository;
import com.training.library.repositories.BookStatusRepository;

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
	@Autowired
	private BookStatusService bookStatusService;
	@Autowired
	private BookStatusRepository bookStautsRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	@Autowired
	private EmailSender emailSender;
	
	public BookReservationResponseDto findBookReservation(Long id) {
		Optional<BookReservationResponseDto> bookReservation = bookReservationRepository
				.findByBookReservationIdAndDeletedAtIsNull(id);
		if (bookReservation.isPresent()) {
			return bookReservation.get();
		}
		return null;
	}

	public Page<BookReservationResponseDto> findAllBookReservation(FilterDto dto, String userId)
			throws NumberFormatException, ParseException {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		if (dto.getStartDate()==null) {
			dto.setStartDate("2020-01-01");
		}
		if (dto.getEndDate()==null) {
			dto.setEndDate(LocalDate.now().plusDays(1000L).toString());
		}
		String pattern = "yyyy-MM-dd";
		if (dto.isUser()) {
			return bookReservationRepository.findAllByDeletedAtIsNull(userId, pageable, Long.parseLong(userId),
					new SimpleDateFormat(pattern).parse(dto.getStartDate()),
					new SimpleDateFormat(pattern).parse(dto.getEndDate()));
		}
		Page<BookReservationResponseDto> result = bookReservationRepository.findAllByDeletedAtIsNull(dto.getSearch(),
				pageable, new SimpleDateFormat(pattern).parse(dto.getStartDate()),
				new SimpleDateFormat(pattern).parse(dto.getEndDate()));
		List<BookReservationResponseDto> content = result.getContent();
		for (BookReservationResponseDto response : content) {
			response.setTotalRequest(getTotalReservations(response.getBookId()));
			response.setBookAvailable(getBookAvailable(response.getBookId()));
			response.setAccptedRequest(getTotalAcceptedReservations(response.getBookId()));
		}
		return result;
	}

	public ResponseEntity<CustomBaseResponseDto> saveBookReservation(BookReservationRequestDto dto, String userName,
			Boolean isUser) {
		if (isUser) {
			Optional<BookReservation> result = bookReservationRepository
					.findByBookDetails_BookDetailsIdAndReserver_PhoneAndDeletedAtIsNull(dto.getBookDetailsId(),
							dto.getPhone());
			if (result.isPresent()) {
				throw new RuntimeException("You already re-reserve the book");
			}
		}
		User reserver = userService.findByPhone(dto.getPhone());
		if (reserver == null) {
			reserver = userService.newUser(dto.getPhone().toString());
		}
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
	public ResponseEntity<CustomBaseResponseDto> saveBookReservationStatus(Long id, Boolean status, Long bookStatusId, String email) {
		Optional<BookReservation> reservationOptional = bookReservationRepository.findById(id);
		Optional<BookStatus> bookStatusOptional = null;
		BookReservation reservation = null;
		if (bookStatusId != null) {
			bookStatusOptional = bookStautsRepository.findById(bookStatusId);
		}
		if (reservationOptional.isPresent()) {
			reservation = reservationOptional.get();
			BookStatus bookStatus = reservation.getBookStatus();
			if (bookStatus != null) {
				bookStatus.setReserved(false);
				bookStatusService.updateReservation(bookStatus);
			}
			reservation.setIsAccepted(status);
			bookReservationRepository.save(reservation);
			
			if (status) {

				if (bookStatusOptional.isPresent()) {
					BookStatus newBookStatus = bookStatusOptional.get();
					reservation.setBookStatus(newBookStatus);
					newBookStatus.setReserved(true);
					bookStatusService.updateReservation(newBookStatus);
//					emailSender.sendSimpleMessage(email, "Reservation is accepted", "hello", "");
				}

			} else {
				bookReservationRepository.deleteByBookReservationId(id);
//				emailSender.sendSimpleMessage(email, "Reservation is rejected", "hello", "");
			}

		}
		return ResponseEntity.ok(new CustomBaseResponseDto(reservation.getReserver().getPhone().toString()));
	}

	public Long getTotalReservations(Long bookDetailsId) {
		return bookReservationRepository.countByBookDetails_BookDetailsIdAndDeletedAtIsNull(bookDetailsId);
	}

	private Boolean getBookAvailable(Long bookId) {
		return bookDetailsService.checkBookAvailable(bookId);
	}

	public Long getTotalAcceptedReservations(Long bookDetailsId) {
		return bookReservationRepository
				.countByBookDetails_BookDetailsIdAndDeletedAtIsNullAndIsAcceptedTrue(bookDetailsId);
	}

	@Transactional
	public void deleteByReservationDate() {
		Date expireDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
		List<BookReservation> result = bookReservationRepository.findAllByReservationDateBefore(expireDate);
		result.forEach(br -> removeReservation(br.getBookStatus()));
		bookReservationRepository.deleteAllByReservationDateBefore(expireDate);

	}

	public void removeReservation(BookStatus bookStatus) {
		bookStatus.setReserved(false);
		bookStatusService.updateBookStatusAvailability(bookStatus);
	}

	public Boolean countBookReservationByUserPhone(Long id) {
		Long counter = bookReservationRepository.countByDeletedAtIsNullAndReserver_Phone(id);
		if (counter < 3) {
			return true;
		}
		return false;
	}

	public Boolean checkReserverByBookStatusId(Long id, Long bookStatusId) {

		Long result = bookReservationRepository.countByReserver_PhoneAndDeletedAtIsNullAndBookStatus_BookStatusId(id,
				bookStatusId);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Transactional
	public void setReservationFinished(Long phone, Long bookStatusId) {
		User user = userService.findByPhone(phone);
		bookReservationRepository.deleteByReservationFinished(user.getUserId(), bookStatusId);
	}

	public ResponseEntity<CustomBaseResponseDto> sendMail(Long id, EmailRequestDto dto) {
		String status = (dto.getStatus()) ? " Accepted" : " Rejected";
		Optional<BookReservation> bookReservationOptional = bookReservationRepository.findById(id);
		BookReservation bookReservation = null;
		if(bookReservationOptional.isPresent()) {
			bookReservation = bookReservationOptional.get();
		}
		String subject = "Your Reservation for book  " + bookReservation.getBookDetails().getTitle();
		String body = (dto.getStatus()) ? "Reservation is"+status+"\n"+"Your assigned bookId: "+bookReservation.getBookReservationId() : "Sorry, But we can't serve your request right now.";
		emailSender.sendSimpleMessage(dto.getEmail(), subject,body);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
}
