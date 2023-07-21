package com.training.library.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.entity.BookBorrowing;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.User;
import com.training.library.repositories.BookBorrowingRepository;

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

	public List<BookBorrowingResponseDto> findAllBookBorrowing() {
		Optional<List<BookBorrowingResponseDto>> bookBorrowingList = bookBorrowingRepository
				.findAllByDeletedAtIsNull();
		if (bookBorrowingList.isPresent()) {
			return bookBorrowingList.get();
		}
		return Collections.emptyList();
	}

	public List<BookBorrowingResponseDto> saveBookBorrowing(BookBorrowingRequestDto dto, String userName) {
		User borrower = userService.findByPhone(dto.getPhone());
		BookStatus bookStatus = bookStatusService.findBookById(dto.getBookStatusId());
		BookBorrowing br = new BookBorrowing();
		br.setBorrower(borrower);
		br.setBookStatus(bookStatus);

		User user = userService.findByPhone(Long.parseLong(userName));

		if (user == null) {
			user = new User();
			user.setPhone(Long.parseLong(userName));
			user = userService.saveUser(user);
		}
		br.setUser(user);
		br.setBorrowingDate(dto.getBorrowingDate());
		bookBorrowingRepository.save(br);
		return findAllBookBorrowing();
	}

	public List<BookBorrowingResponseDto> updateBookBorrowing(Long id, BookBorrowingRequestDto dto) {
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
		return findAllBookBorrowing();
	}

	public List<BookBorrowingResponseDto> deleteBookBorrowing(Long id) {
		bookBorrowingRepository.deleteByBookBorrowingId(id);
		return findAllBookBorrowing();
	}
}
