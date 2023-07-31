package com.training.library.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookReservation;
import com.training.library.entity.User;
import com.training.library.repositories.BookReservationRepository;

@Service
public class BookReservationService {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BookReservationRepository bookReservationRepository;
	@Autowired
	private BookDetailsService bookDetailsService;
	@Autowired
	private UserService userService;

	public BookReservationResponseDto findBookReservation(Long id) {
		Optional<BookReservationResponseDto> bookReservation = bookReservationRepository
				.findByBookReservationIdAndDeletedAtIsNull(id);
		if (bookReservation.isPresent()) {
			return bookReservation.get();
		}
		return null;
	}

	public Page<BookReservationResponseDto> findAllBookReservation(FilterDto dto) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return bookReservationRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageable);
	}

	public void saveBookReservation(BookReservationRequestDto dto, String userName) {
		User reserver = userService.findByPhone(dto.getPhone());
		BookDetails bookDetails = bookDetailsService.findBookDetailsById(dto.getBookDetailsId());
		BookReservation br = new BookReservation();
		br.setReserver(reserver);
		br.setBookDetails(bookDetails);

		User user = userService.findByPhone(Long.parseLong("9725953035"));

		if (user == null) {
			user = new User();
			user.setPhone(Long.parseLong(userName));
			user = userService.saveUser(user);
		}
		br.setUser(user);
		br.setReservationDate(dto.getReservationDate());
		bookReservationRepository.save(br);
	}

	public void updateBookReservation(Long id, BookReservationRequestDto dto) {
		User reserver = userService.findByPhone(dto.getPhone());
		BookDetails bookDetails = bookDetailsService.findBookDetailsById(dto.getBookDetailsId());
		Optional<BookReservation> brOptional = bookReservationRepository.findById(id);
		if (brOptional.isPresent()) {
			BookReservation br = new BookReservation();
			br.setReserver(reserver);
			br.setBookDetails(bookDetails);
			br.setReservationDate(dto.getReservationDate());
			bookReservationRepository.save(br);
		}

	}

	public void deleteBookReservation(Long id) {
		bookReservationRepository.deleteByBookReservationId(id);

	}
}
