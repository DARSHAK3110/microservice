package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
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

import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookReservation;
import com.training.library.entity.User;
import com.training.library.repositories.BookReservationRepository;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.ShelfRepository;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookReservationServiceTest {
	@Mock
	private ShelfRepository shelfRepository;

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private BookReservationService service;
	@Mock
	private BookReservationRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;
	@Mock
	private UserService userService;
	@Mock
	private BookDetailsService bookDetailsService;
	@Mock
	private Environment env;
	
	@Test
	void findBookReservationTest1() {
		when(repo.findByBookReservationIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getBookReservationResponseDto(0L)));
		BookReservationResponseDto result = service.findBookReservation(0L);
		assertThat(result.getBookReservationId()).isEqualTo(0L);
	}

	@Test
	void findBookReservationTest2() {
		when(repo.findByBookReservationIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		BookReservationResponseDto result = service.findBookReservation(0L);
		assertThat(result).isNull();
	}

	@Test
	void findAllBookReservationTest1() {
		FilterDto dto = entityGenerator.getFilterDto();
		dto.setUser(true);
		Page<BookReservationResponseDto> actual = entityGenerator.getBookReservationPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull("1231231231", pageable,Long.parseLong("1231231231"))).thenReturn(actual);
		Page<BookReservationResponseDto> result = service.findAllBookReservation(dto,"1231231231");
		assertThat(result).isEqualTo(actual);
	}
	
	@Test
	void findAllBookReservationTest2() {
		FilterDto dto = entityGenerator.getFilterDto();
		dto.setUser(false);
		Page<BookReservationResponseDto> actual = entityGenerator.getBookReservationPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable)).thenReturn(actual);
		Page<BookReservationResponseDto> result = service.findAllBookReservation(dto,"1231231231");
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void saveBookReservationTest1() {
		BookReservationRequestDto req = entityGenerator.getBookReservationRequestDto();
		User user = entityGenerator.getMockUser();
		when(userService.findByPhone(1231231231L)).thenReturn(null);
		when(userService.newUser("1231231231")).thenReturn(user);
		when(bookDetailsService.findBookDetailsById(any(Long.class))).thenReturn(entityGenerator.getMockBookDetails());
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservation(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookReservationTest2() {
		BookReservationRequestDto req = entityGenerator.getBookReservationRequestDto();
		User user = entityGenerator.getMockUser();
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(bookDetailsService.findBookDetailsById(any(Long.class))).thenReturn(entityGenerator.getMockBookDetails());
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservation(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void testDeleteBookReservation() {
		ResponseEntity<CustomBaseResponseDto> result = service.deleteBookReservation(0L);
		assertThat(result).isNotNull();
	}

	
	@Test
	void checkReservationTest1() {
		when(repo.findByBookDetails_BookDetailsIdAndReserver_PhoneAndDeletedAtIsNull(any(Long.class),any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockBookReservation()));
		boolean result = service.checkReservation(0L,"1231231231");
		assertThat(result).isTrue();
	}

	@Test
	void checkReservationTest2() {
		when(repo.findByBookDetails_BookDetailsIdAndReserver_PhoneAndDeletedAtIsNull(any(Long.class),any(Long.class))).thenReturn(Optional.empty());
		boolean result = service.checkReservation(0L,"1231231231");
		assertThat(result).isFalse();
	}

	@Test
	void saveBookReservationStatusTest1() {
		when(repo.findById(any(Long.class))).thenReturn(Optional.empty());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservationStatus(0L, true);
		assertThat(result).isNotNull();
	}
	
	@Test
	void saveBookReservationStatusTest2() {
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockBookReservation()));
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservationStatus(0L, true);
		assertThat(result).isNotNull();
	}
	@Test
	void saveBookReservationStatusTest3() {
		when(repo.findById(any(Long.class))).thenReturn(Optional.empty());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservationStatus(0L, false);
		verify(repo,times(1)).deleteByBookReservationId(0L);
		assertThat(result).isNotNull();
	}
	
	@Test
	void deleteByReservationDateTest1() {
		service.deleteByReservationDate();
		verify(repo,times(1)).deleteAllByReservationDateBefore(any(Date.class));
	}
	
}
