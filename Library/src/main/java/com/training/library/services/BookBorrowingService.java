package com.training.library.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookBorrowing;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.User;
import com.training.library.repositories.BookBorrowingRepository;

import jakarta.transaction.Transactional;

@Service
@PropertySource("classpath:message.properties")
public class BookBorrowingService {

	@Autowired
	private BookBorrowingRepository bookBorrowingRepository;
	@Autowired
	private BookStatusService bookStatusService;
	@Autowired
	private BookDetailsService bookDetailService;
	@Autowired
	private BookReservationService bookReservationService;
	@Autowired
	private UserService userService;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	public BookBorrowingResponseDto findBookBorrowing(Long id) {
		Optional<BookBorrowingResponseDto> bookBorrowing = bookBorrowingRepository
				.findByBookBorrowingIdAndDeletedAtIsNull(id);
		if (bookBorrowing.isPresent()) {
			return bookBorrowing.get();
		}
		return null;
	}

	public Page<BookBorrowingResponseDto> findAllBookBorrowing(FilterDto dto, String userName) throws ParseException {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		if (dto.getStartDate() == null) {
			dto.setStartDate("2020-01-01");
		}
		if (dto.getEndDate() == null) {
			dto.setEndDate(LocalDate.now().plusDays(3).toString());
		}
		if (!dto.isDeletedAt()) {
			if (dto.isUser()) {
				return bookBorrowingRepository.findAllwithSearchByBorrowingDate(dto.getSearch(), pageble, userName,
						new SimpleDateFormat("yyyy-MM-dd").parse(dto.getStartDate()),
						new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEndDate()));
			}
			return bookBorrowingRepository.findAllwithSearchBorrowingDate(dto.getSearch(), pageble,
					new SimpleDateFormat("yyyy-MM-dd").parse(dto.getStartDate()),
					new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEndDate()));
		} else {
			if (dto.isUser()) {
				return bookBorrowingRepository.findAllwithSearchByReturnDate(dto.getSearch(), pageble, userName,
						new SimpleDateFormat("yyyy-MM-dd").parse(dto.getStartDate()),
						new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEndDate()));
			}
			return bookBorrowingRepository.findAllwithSearchReturnDate(dto.getSearch(), pageble,
					new SimpleDateFormat("yyyy-MM-dd").parse(dto.getStartDate()),
					new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEndDate()));
		}
	}

	public ResponseEntity<CustomBaseResponseDto> saveBookBorrowing(BookBorrowingRequestDto dto, String userName) {
		User borrower = userService.findByPhone(dto.getPhone());
		BookStatus bookStatus = bookStatusService.findBookById(dto.getBookStatusId());
		bookStatus.setAvailable(false);
		bookStatus.setReserved(false);
		bookStatusService.updateBookStatusAvailability(bookStatus);
		BookBorrowing br = new BookBorrowing();
		br.setBorrower(borrower);
		br.setBookStatus(bookStatus);
		BookDetails bookDetails = bookStatus.getBookDetails();
		bookDetailService.setAvailableCopies(bookDetails, "checkIn");
		User user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		br.setUser(user);
		bookBorrowingRepository.save(br);
		if(dto.getIsReserved()) {
			bookReservationService.setReservationFinished(dto.getPhone(),dto.getBookStatusId());	
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteBookBorrowing(Long id) {
		BookBorrowingResponseDto bookBorrowing = null;
		Optional<BookBorrowingResponseDto> bookBorrowingOptional = bookBorrowingRepository
				.findByBookBorrowingIdAndDeletedAtIsNull(id);
		if (bookBorrowingOptional.isPresent()) {
			bookBorrowing = bookBorrowingOptional.get();
			BookStatus bookStatus = this.bookStatusService.findBookById(bookBorrowing.getBookId());
			bookStatus.setAvailable(true);
			BookDetails bookDetails = bookStatus.getBookDetails();
			bookDetailService.setAvailableCopies(bookDetails, "checkOut");
			this.bookStatusService.updateBookStatusAvailability(bookStatus);
			bookBorrowingRepository.deleteByBookBorrowingId(id);
		}

		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public BookBorrowingResponseDto findBookBorrowingByBookStatus(Long id) {
		Optional<BookBorrowingResponseDto> bookBorrowing = bookBorrowingRepository
				.findByBookStatus_BookStatusIdAndDeletedAtIsNull(id);
		if (bookBorrowing.isPresent()) {
			return bookBorrowing.get();
		}
		return null;
	}

	public Boolean countBookBorrowingByUserPhone(Long id) {

		Long counter = bookBorrowingRepository.countByDeletedAtIsNullAndBorrower_Phone(id);
		if(counter<3) {
			return true;
		}
		return false;
	}
}