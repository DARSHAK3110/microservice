package com.training.library.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.entity.BookBorrowing;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.User;
import com.training.library.repositories.BookBorrowingRepository;

import jakarta.transaction.Transactional;

@Service
public class BookBorrowingService {

	@Autowired
	private BookBorrowingRepository bookBorrowingRepository;
	@Autowired
	private BookStatusService bookStatusService;
	@Autowired
	private UserService userService;

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
		return bookBorrowingRepository.findAllwithSearch(dto.getSearch(),  pageble);
	}

	public void saveBookBorrowing(BookBorrowingRequestDto dto, String userName) {
		User borrower = userService.findByPhone(dto.getPhone());
		BookStatus bookStatus = bookStatusService.findBookById(dto.getBookStatusId());
		bookStatus.setAvailable(false);
		bookStatusService.updateBookStatusAvailability(bookStatus);
		BookBorrowing br = new BookBorrowing();
		br.setBorrower(borrower);
		br.setBookStatus(bookStatus);

		User user = userService.findByPhone(Long.parseLong("9725953035"));

		if (user == null) {
			user = new User();
			user.setPhone(Long.parseLong(userName));
			user = userService.saveUser(user);
		}
		br.setUser(user);
		br.setBorrowingDate(dto.getBorrowingDate());
		bookBorrowingRepository.save(br);

	}

	public void updateBookBorrowing(Long id, BookBorrowingRequestDto dto) {
		User borrower = userService.findByPhone(dto.getPhone());
		BookStatus bookStatus = bookStatusService.findBookById(dto.getBookStatusId());
		Optional<BookBorrowing> brOptional = bookBorrowingRepository.findById(id);
		if (brOptional.isPresent()) {
			BookBorrowing br = new BookBorrowing();
			br.setBorrower(borrower);
			br.setBookStatus(bookStatus);
			br.setBorrowingDate(dto.getBorrowingDate());
			bookBorrowingRepository.save(br);
		}
	}

	@Transactional
	public void deleteBookBorrowing(Long id) {
		BookBorrowingResponseDto bookBorrowing = findBookBorrowing(id);
		BookStatus bookStatus = this.bookStatusService.findBookById(bookBorrowing.getBookId());
		bookStatus.setAvailable(true);
		this.bookStatusService.updateBookStatusAvailability(bookStatus);
		bookBorrowingRepository.deleteByBookBorrowingId(id);
		
		
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