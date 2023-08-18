package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
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

import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Location;
import com.training.library.repositories.BookStatusRepository;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.UploadRepository;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookStatusServiceTest {

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private BookStatusService service;
	@Mock
	private BookStatusRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;
	@Mock
	private UserService userService;

	@Mock
	private UploadRepository uploadRepository;
	@Mock
	private LocationService locationService;

	@Mock
	private BookDetailsService bookDetailsService;
	@Mock
	private Environment env;

	@Test
	void findBookStatusTest1() {
		when(repo.findByBookStatusIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getBookStatusResponseDto(0L)));		BookStatusResponseDto result = service.findBookStatus(0L);
		assertThat(result.getBookStatusId()).isEqualTo(0L);
	}

	@Test
	void findBookStatusTest2() {
		when(repo.findByBookStatusIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		BookStatusResponseDto result = service.findBookStatus(0L);
		assertThat(result).isNull();
	}

	@Test
	void deleteBookStatusTest() {
		BookStatus mockBookStatus = entityGenerator.getMockBookStatus();
		mockBookStatus.setBookStatusId(0L);
		mockBookStatus.getBookDetails().setBookDetailsId(0L);
		when(repo.findById(anyLong())).thenReturn(Optional.of(mockBookStatus));
		when(locationService.findLocationById(any(Long.class))).thenReturn(entityGenerator.getMockLocation());
		ResponseEntity<CustomBaseResponseDto> result = service.deleteBookStatus(0L);
		assertThat(result).isNotNull();
	}
	
	@Test
	void deleteBookStatusTest3() {
		BookStatus mockBookStatus = entityGenerator.getMockBookStatus();
		mockBookStatus.setBookStatusId(0L);
		mockBookStatus.setAvailable(false);
		mockBookStatus.getBookDetails().setBookDetailsId(0L);
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> service.deleteBookStatus(mockBookStatus));
		assertThat(exception.getMessage()).isEqualTo("For delete book, book really required at location");
	}

	
	@Test
	void deleteBookStatusTest4() {
		BookStatus mockBookStatus = entityGenerator.getMockBookStatus();
		mockBookStatus.setBookStatusId(0L);
		mockBookStatus.setAvailable(false);
		mockBookStatus.getBookDetails().setBookDetailsId(0L);
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> service.deleteBookStatus(mockBookStatus));
		assertThat(exception.getMessage()).isEqualTo("For delete book, book really required at location");
	}
	@Test
	void deleteBookStatusTest2() {
		when(locationService.findLocationById(entityGenerator.getMockBookStatus().getLocation().getLocationId())).thenReturn(entityGenerator.getMockLocation());
		ResponseEntity<CustomBaseResponseDto> result = service.deleteBookStatus(entityGenerator.getMockBookStatus());
		assertThat(result).isNotNull();
	}

	@Test
	void updateBookStatusTest1() {
		when(repo.findById(any(Long.class))).thenReturn(Optional.empty());
		BookStatusRequestDto req = entityGenerator.getBookStatusRequestDto();
		ResponseEntity<CustomBaseResponseDto> result = service.updateBookStatus(0L, req);
		assertThat(result).isNotNull();
	}

	@Test
	void updateBookStatusTest2() {
		Location location = entityGenerator.getMockLocation();
		BookStatusRequestDto req = entityGenerator.getBookStatusRequestDto();
		location.setLocationId(0L);
		BookStatus bookStatus = entityGenerator.getMockBookStatus();
		bookStatus.setLocation(location);
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(bookStatus));
		when(locationService.findLocationById(any(Long.class))).thenReturn(location);
		ResponseEntity<CustomBaseResponseDto> result = service.updateBookStatus(0L, req);
		assertThat(result).isNotNull();
	}

	@Test
	void findBookByIdTest1() {
		BookStatus mockBookStatus = entityGenerator.getMockBookStatus();
		mockBookStatus.setBookStatusId(0L);
		Optional<BookStatus> bookStatus = Optional.of(mockBookStatus);
		bookStatus.get().setBookStatusId(0L);
		when(repo.findById(any(Long.class))).thenReturn(bookStatus);
		BookStatus result = service.findBookById(0L);
		assertThat(result.getBookStatusId()).isEqualTo(0L);
	}

	@Test
	void findBookByIdTest2() {
		when(repo.findById(0L)).thenReturn(Optional.empty());
		BookStatus result = service.findBookById(0L);
		assertThat(result).isNull();
	}

	@Test
	void findAllBookStatusByBookDetailsIdTest1() {
		FilterDto dto = entityGenerator.getFilterDto();
		dto.setAvailability(-1);
		Pageable pageble = PageRequest.of(0, 2);
		when(repo.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(pageble, 0L, false)).thenReturn(null);
		Page<BookStatusResponseDto> result = service.findAllBookStatusByBookDetailsId(dto, 0L);
		assertThat(result).isNull();
	}

	@Test
	void findAllBookStatusByBookDetailsIdTest2() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<BookStatusResponseDto> bookStatusPage = entityGenerator.getBookStatusPage();
		dto.setAvailability(1);
		Pageable pageble = PageRequest.of(0, 2);
		when(repo.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(pageble, 0L, true)).thenReturn(bookStatusPage);
		Page<BookStatusResponseDto> result = service.findAllBookStatusByBookDetailsId(dto, 0L);
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void findAllBookStatusByBookDetailsIdTest3() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<BookStatusResponseDto> bookStatusPage = entityGenerator.getBookStatusPage();
		dto.setAvailability(0);
		Pageable pageble = PageRequest.of(0, 2);
		when(repo.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(pageble, 0L)).thenReturn(bookStatusPage);
		Page<BookStatusResponseDto> result = service.findAllBookStatusByBookDetailsId(dto, 0L);
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testUpdateBookStatusAvailability() {
		BookStatus bookStatus = entityGenerator.getMockBookStatus();
		when(repo.save(any(BookStatus.class))).thenReturn(bookStatus);
		service.updateBookStatusAvailability(bookStatus);
		verify(repo, times(1)).save(bookStatus);
	}

	@Test
	void findByLocationIdTest1() {
		when(repo.findByLocation_LocationId(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockBookStatus()));
		BookStatus result = service.findByLocationId(0L);
		assertThat(result).isNotNull();
	}

	@Test
	void findByLocationIdTest2() {
		when(repo.findByLocation_LocationId(any(Long.class))).thenReturn(Optional.empty());
		BookStatus result = service.findByLocationId(0L);
		assertThat(result).isNull();
	}

	@Test
	void getCountByLocationTest() {
		service.getCountByLocation(0L);
		verify(repo, times(1)).countByLocation_LocationIdAndDeletedAtIsNull(anyLong());
	}

	@Test
	void getCountByShelfTest() {
		service.getCountByShelf(0L);
		verify(repo, times(1)).countByLocation_Shelf_ShelfIdAndDeletedAtIsNull(anyLong());
	}

	@Test
	void getCountBySectionTest() {
		service.getCountBySection(0L);
		verify(repo, times(1)).countByLocation_Shelf_Section_SectionIdAndDeletedAtIsNull(anyLong());
	}

	@Test
	void getCountByFloorTest() {
		service.getCountByFloor(0L);
		verify(repo, times(1)).countByLocation_Shelf_Section_Floor_FloorIdAndDeletedAtIsNull(anyLong());
	}

	@Test
	void testFindAllBooksByLocation() {
		service.findAllBooksByLocation(0L, entityGenerator.getFilterDto());
		verify(repo, times(1)).findAllByLocationId(anyLong(), any(Pageable.class));
	}

	@Test
	void testFindAllBooksByShelf() {
		service.findAllBooksByShelf(0L, entityGenerator.getFilterDto());
		verify(repo, times(1)).findAllByShelfId(anyLong(), any(Pageable.class));
	}

	@Test
	void testFindAllBooksByFloor() {
		service.findAllBooksByFloor(0L, entityGenerator.getFilterDto());
		verify(repo, times(1)).findAllByFloorId(anyLong(), any(Pageable.class));
	}

	@Test
	void testFindAllBooksBySection() {
		service.findAllBooksBySection(0L, entityGenerator.getFilterDto());
		verify(repo, times(1)).findAllBySectionId(anyLong(), any(Pageable.class));
	}
}
