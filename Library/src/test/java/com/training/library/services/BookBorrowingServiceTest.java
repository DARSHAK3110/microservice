package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookBorrowing;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.User;
import com.training.library.repositories.BookBorrowingRepository;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookBorrowingServiceTest {

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private BookBorrowingService service;
	@Mock
	private BookBorrowingRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;
	@Mock
	private UserService userService;
	@Mock
	private BookStatusService bookStatusService;
	@Mock
	private BookDetailsService bookDetailService;
	@Mock
	private Environment env;

	@Test
	void findBookBorrowingTest1() {
		when(repo.findByBookBorrowingIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getBookBorrowingResponseDto(0L)));
		BookBorrowingResponseDto result = service.findBookBorrowing(0L);
		assertThat(result.getBookBorrowingId()).isEqualTo(0L);
	}

	@Test
	void findBookBorrowingTest2() {
		when(repo.findByBookBorrowingIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		BookBorrowingResponseDto result = service.findBookBorrowing(0L);
		assertThat(result).isNull();
	}

	@Test
	void testFindAllBookBorrowing() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<BookBorrowingResponseDto> actual = entityGenerator.getBookBorrowingPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllwithSearch(dto.getSearch(), pageable)).thenReturn(actual);
		Page<BookBorrowingResponseDto> result = service.findAllBookBorrowing(dto, "1231231231");
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void saveBookBorrowingTest1() {
		BookBorrowingRequestDto req = entityGenerator.getBookBorrowingRequestDto();
		User user = entityGenerator.getMockUser();
		when(userService.findByPhone(1231231231L)).thenReturn(null);
		when(userService.newUser("1231231231")).thenReturn(user);
		when(bookStatusService.findBookById(any(Long.class))).thenReturn(entityGenerator.getMockBookStatus());
		doNothing().when(bookDetailService).setAvailableCopies(any(BookDetails.class), any(String.class));
		when(repo.save(any(BookBorrowing.class))).thenReturn(entityGenerator.getMockBookBorrowing());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookBorrowing(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookBorrowingTest2() {
		BookBorrowingRequestDto req = entityGenerator.getBookBorrowingRequestDto();
		User user = entityGenerator.getMockUser();
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(bookStatusService.findBookById(any(Long.class))).thenReturn(entityGenerator.getMockBookStatus());
		when(repo.save(any(BookBorrowing.class))).thenReturn(entityGenerator.getMockBookBorrowing());
		doNothing().when(bookDetailService).setAvailableCopies(any(BookDetails.class), any(String.class));
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookBorrowing(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void findAllBookBorrowingTest2() {
		FilterDto dto = entityGenerator.getFilterDto();
		dto.setUser(true);
		Page<BookBorrowingResponseDto> actual = entityGenerator.getBookBorrowingPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllwithSearch(dto.getSearch(), pageable, "1231231231")).thenReturn(actual);
		Page<BookBorrowingResponseDto> result = service.findAllBookBorrowing(dto, "1231231231");
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void deleteBookBorrowingTest1() {
		BookStatus mockBookStatus = entityGenerator.getMockBookStatus();
		BookBorrowingResponseDto dto = entityGenerator.getBookBorrowingResponseDto(0L);
		dto.setBookId(0L);
		when(repo.findByBookBorrowingIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(dto));
		mockBookStatus.setBookStatusId(0L);
		when(bookStatusService.findBookById(any(Long.class))).thenReturn(entityGenerator.getMockBookStatus());
		ResponseEntity<CustomBaseResponseDto> result = service.deleteBookBorrowing(0L);
		verify(repo, times(1)).deleteByBookBorrowingId(0L);
		assertThat(result).isNotNull();
	}
	
	@Test
	void deleteBookBorrowingTest2() {
		when(repo.findByBookBorrowingIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		service.deleteBookBorrowing(0L);
		verify(repo, times(0)).deleteByBookBorrowingId(0L);
	}

	@Test
	void findBookBorrowingByBookStatusTest1() {
		BookBorrowingResponseDto dto = entityGenerator.getBookBorrowingResponseDto(0L);
		dto.setBookId(0L);
		when(repo.findByBookStatus_BookStatusIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(dto));
		BookBorrowingResponseDto result = service.findBookBorrowingByBookStatus(0L);
		assertThat(result.getBookId()).isEqualTo(0L);
	}

	@Test void findBookBorrowingByBookStatusTest2() { 
		  when(repo.findByBookStatus_BookStatusIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		  BookBorrowingResponseDto result = service.findBookBorrowingByBookStatus(0L);
		  assertThat(result).isNull();
	  }

}
