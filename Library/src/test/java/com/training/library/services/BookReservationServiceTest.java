package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.training.library.entity.BookStatus;
import com.training.library.entity.User;
import com.training.library.repositories.BookReservationRepository;
import com.training.library.repositories.BookStatusRepository;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.ShelfRepository;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookReservationServiceTest {
	@Mock
	private ShelfRepository shelfRepository;
	@Mock
	private BookStatusService bookStatusService;
	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private BookReservationService service;
	@Mock
	private BookReservationRepository repo;
	@Mock
	private BookStatusRepository bookStatusRepository;
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
	void findAllBookReservationTest1() throws NumberFormatException, ParseException {
		FilterDto dto = entityGenerator.getFilterDto();
		dto = entityGenerator.setDate(dto);
		dto.setUser(true);
		Page<BookReservationResponseDto> actual = entityGenerator.getBookReservationPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull("1231231231", pageable, Long.parseLong("1231231231"),
				new SimpleDateFormat("yyyy-MM-dd").parse(dto.getStartDate()),
				new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEndDate()))).thenReturn(actual);
		Page<BookReservationResponseDto> result = service.findAllBookReservation(dto, "1231231231");
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void findAllBookReservationTest2() throws NumberFormatException, ParseException {
		FilterDto dto = entityGenerator.getFilterDto();
		dto = entityGenerator.setDate(dto);
		dto.setUser(false);
		Page<BookReservationResponseDto> actual = entityGenerator.getBookReservationPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable,
				new SimpleDateFormat("yyyy-MM-dd").parse(dto.getStartDate()),
				new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEndDate()))).thenReturn(actual);
		Page<BookReservationResponseDto> result = service.findAllBookReservation(dto, "1231231231");
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void findAllBookReservationTest3() throws NumberFormatException, ParseException {
		FilterDto dto = entityGenerator.getFilterDto();
		dto.setUser(false);
		Page<BookReservationResponseDto> actual = entityGenerator.getBookReservationPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNull(dto.getSearch(), pageable,
				new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"),
				new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(1000L).toString())))
				.thenReturn(actual);
		Page<BookReservationResponseDto> result = service.findAllBookReservation(dto, "1231231231");
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
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservation(req, "1231231231", true);
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookReservationTest3() {
		BookReservationRequestDto req = entityGenerator.getBookReservationRequestDto();
		User user = entityGenerator.getMockUser();
		when(userService.findByPhone(1231231231L)).thenReturn(null);
		when(userService.newUser("1231231231")).thenReturn(user);
		when(bookDetailsService.findBookDetailsById(any(Long.class))).thenReturn(entityGenerator.getMockBookDetails());
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservation(req, "1231231231", false);
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookReservationTest2() {
		BookReservationRequestDto req = entityGenerator.getBookReservationRequestDto();
		User user = entityGenerator.getMockUser();
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(bookDetailsService.findBookDetailsById(any(Long.class))).thenReturn(entityGenerator.getMockBookDetails());
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservation(req, "1231231231", true);
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
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservationStatus(0L, true,0L);
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookReservationStatusTest2() {
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockBookReservation()));
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservationStatus(0L, true,0L);
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookReservationStatusTest3() {
		BookReservation reservation = entityGenerator.getMockBookReservation();
		reservation.setBookStatus(entityGenerator.getMockBookStatus());
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(reservation));
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.of(entityGenerator.getMockBookStatus()));
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservationStatus(0L, false, 0L);
		verify(repo, times(1)).deleteByBookReservationId(0L);
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookReservationStatusTest4() {
		BookReservation reservation = entityGenerator.getMockBookReservation();
		reservation.setBookStatus(entityGenerator.getMockBookStatus());
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(reservation));
		when(repo.save(any(BookReservation.class))).thenReturn(entityGenerator.getMockBookReservation());
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.of(entityGenerator.getMockBookStatus()));
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookReservationStatus(0L, true, 0L);
		verify(repo, times(0)).deleteByBookReservationId(0L);
		assertThat(result).isNotNull();
	}

	@Test
	void deleteByReservationDateTest1() {
		service.deleteByReservationDate();
		verify(repo, times(1)).deleteAllByReservationDateBefore(any(Date.class));
	}

	@Test
	void removeReservationTest1() {
		service.removeReservation(entityGenerator.getMockBookStatus());
		verify(bookStatusService, times(1)).updateBookStatusAvailability(any(BookStatus.class));
	}

	@Test
	void countBookReservationByUserPhoneTest1() {
		when(repo.countByDeletedAtIsNullAndReserver_Phone(any(Long.class))).thenReturn(0L);
		Boolean result = service.countBookReservationByUserPhone(1231231231L);
		assertThat(result).isTrue();
	}

	@Test
	void countBookReservationByUserPhoneTest2() {
		when(repo.countByDeletedAtIsNullAndReserver_Phone(any(Long.class))).thenReturn(3L);
		Boolean result = service.countBookReservationByUserPhone(1231231231L);
		assertThat(result).isFalse();
	}

	@Test
	void checkReserverByBookStatusIdTest1() {
		when(repo.countByReserver_PhoneAndDeletedAtIsNullAndBookStatus_BookStatusId(any(Long.class),any(Long.class))).thenReturn(1L);
		Boolean result = service.checkReserverByBookStatusId(0L,1231231231L);
		assertThat(result).isTrue();
	}

	@Test
	void checkReserverByBookStatusIdTest2() {
		when(repo.countByReserver_PhoneAndDeletedAtIsNullAndBookStatus_BookStatusId(any(Long.class),any(Long.class))).thenReturn(0L);
		Boolean result = service.checkReserverByBookStatusId(0L,1231231231L);
		assertThat(result).isFalse();
	}

	@Test
	void setReservationFinishedTest1() {
		User mockUser = entityGenerator.getMockUser();
		mockUser.setUserId(0L);
		when(userService.findByPhone(any(Long.class))).thenReturn(mockUser);
		service.setReservationFinished(1231231231L, 0L);
		verify(repo, times(1)).deleteByReservationFinished(0L, 0L);
	}

}
