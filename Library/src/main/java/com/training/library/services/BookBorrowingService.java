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

	public Page<BookBorrowingResponseDto> findAllBookBorrowing(FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return bookBorrowingRepository.findAllwithSearch(dto.getSearch(), pageble);
	}

	public ResponseEntity<CustomBaseResponseDto> saveBookBorrowing(BookBorrowingRequestDto dto, String userName) {
		User borrower = userService.findByPhone(dto.getPhone());
		BookStatus bookStatus = bookStatusService.findBookById(dto.getBookStatusId());
		bookStatus.setAvailable(false);
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
		br.setBorrowingDate(dto.getBorrowingDate());
		bookBorrowingRepository.save(br);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteBookBorrowing(Long id) {
		BookBorrowingResponseDto bookBorrowing = findBookBorrowing(id);
		BookStatus bookStatus = this.bookStatusService.findBookById(bookBorrowing.getBookId());
		bookStatus.setAvailable(true);
		BookDetails bookDetails = bookStatus.getBookDetails();
		bookDetailService.setAvailableCopies(bookDetails, "checkOut");
		this.bookStatusService.updateBookStatusAvailability(bookStatus);
		bookBorrowingRepository.deleteByBookBorrowingId(id);
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
}