package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.Author;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Location;
import com.training.library.entity.Upload;
import com.training.library.entity.User;
import com.training.library.repositories.BookDetailsRepository;
import com.training.library.repositories.BookStatusRepository;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.UploadRepository;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookDetailsServiceTest {

	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private BookDetailsService service;
	@Mock
	private BookDetailsRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;
	@Mock
	private UserService userService;
	@Mock
	private AuthorService authorService;
	@Mock
	private UploadRepository uploadRepository;
	@Mock
	private BookStatusRepository bookStatusRepository;
	@Mock
	private LocationService locationService;
	@Mock
	private Environment env;

	@Test
	void findBookDetailsTest1() {
		when(repo.findByBookDetailsIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getBookDetailsResponseDto(0L)));
		BookDetailsResponseDto result = service.findBookDetails(0L);
		assertThat(result.getBookDetailsId()).isEqualTo(0L);
	}

	@Test
	void findBookDetailsTest2() {
		when(repo.findByBookDetailsIdAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		BookDetailsResponseDto result = service.findBookDetails(0L);
		assertThat(result).isNull();
	}

	@Test
	void testFindAllBookDetails() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<BookDetailsResponseDto> actual = entityGenerator.getBookDetailsPage();
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		when(repo.findAllByDeletedAtIsNullAndTitleIgnoreCaseContainingOrAuthor_AuthorNameIgnoreCaseContaining(
				dto.getSearch(), dto.getSearch(), pageable)).thenReturn(actual);
		Page<BookDetailsResponseDto> result = service.findAllBookDetails(dto);
		assertThat(result).isEqualTo(actual);
	}

	@Test
	void saveBookDetailsTest1() {
		BookDetailsRequestDto req = entityGenerator.getBookDetailsRequestDto();
		User user = entityGenerator.getMockUser();
		Author author = entityGenerator.getMockAuthor();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(user);
		when(authorService.findAuthorByAuthorId(req.getAuthorId())).thenReturn(author);
		when(uploadRepository.save(any(Upload.class))).thenReturn(entityGenerator.getMockUpload());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookDetails(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookDetailsTest2() {
		BookDetailsRequestDto req = entityGenerator.getBookDetailsRequestDto();
		User user = entityGenerator.getMockUser();
		Author author = entityGenerator.getMockAuthor();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(null);
		when(userService.newUser("1231231231")).thenReturn(user);
		when(authorService.findAuthorByAuthorId(req.getAuthorId())).thenReturn(author);
		when(uploadRepository.save(any(Upload.class))).thenReturn(entityGenerator.getMockUpload());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookDetails(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void saveBookDetailsTest3() {
		BookDetailsRequestDto req = entityGenerator.getBookDetailsRequestDto();
		User user = entityGenerator.getMockUser();
		user.setUserId(any(Long.class));
		when(userService.findByPhone(123123123L)).thenReturn(user);
		when(authorService.findAuthorByAuthorId(req.getAuthorId())).thenReturn(null);
		when(uploadRepository.save(any(Upload.class))).thenReturn(entityGenerator.getMockUpload());
		ResponseEntity<CustomBaseResponseDto> result = service.saveBookDetails(req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void updateBookDetailsTest1() {
		User user = entityGenerator.getMockUser();
		Location location = entityGenerator.getMockLocation();
		BookDetails bookDetails = entityGenerator.getMockBookDetails();
		BookStatus bookStatus = entityGenerator.getMockBookStatus();
		BookDetailsRequestDto req = entityGenerator.getBookDetailsRequestDto();
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(repo.findById(0L)).thenReturn(Optional.of(bookDetails));
		ResponseEntity<CustomBaseResponseDto> result = service.updateBookDetails(0L, req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void updateBookDetailsTest2() {
		User user = entityGenerator.getMockUser();
		Location location = entityGenerator.getMockLocation();
		location.setLocationId(0L);
		BookDetails bookDetails = entityGenerator.getMockBookDetails();
		BookStatus bookStatus = entityGenerator.getMockBookStatus();
		bookStatus.setLocation(location);
		BookDetailsRequestDto req = entityGenerator.getBookDetailsRequestDto();
		when(bookStatusRepository.findById(bookStatus.getBookStatusId())).thenReturn(Optional.of(bookStatus));
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(bookDetails));
		when(userService.findByPhone(1231231231L)).thenReturn(user);
		when(locationService.findLocationById(any(Long.class))).thenReturn(location);
		req.setBookStatus(entityGenerator.getBookStatusRequestDto());
		ResponseEntity<CustomBaseResponseDto> result = service.updateBookDetails(0L, req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void updateBookDetailsTest3() {
		User user = entityGenerator.getMockUser();
		Location location = entityGenerator.getMockLocation();
		location.setLocationId(0L);
		BookDetails bookDetails = entityGenerator.getMockBookDetails();
		BookStatus bookStatus = entityGenerator.getMockBookStatus();
		bookStatus.setLocation(location);
		BookDetailsRequestDto req = entityGenerator.getBookDetailsRequestDto();
		when(bookStatusRepository.findById(bookStatus.getBookStatusId())).thenReturn(Optional.empty());
		when(repo.findById(any(Long.class))).thenReturn(Optional.of(bookDetails));
		when(userService.findByPhone(1231231231L)).thenReturn(null);
		when(userService.newUser("1231231231")).thenReturn(user);
		when(locationService.findLocationById(any(Long.class))).thenReturn(location);
		req.setBookStatus(entityGenerator.getBookStatusRequestDto());
		ResponseEntity<CustomBaseResponseDto> result = service.updateBookDetails(0L, req, "1231231231");
		assertThat(result).isNotNull();
	}

	@Test
	void testDeleteBookDetails() {
		ResponseEntity<CustomBaseResponseDto> result = service.deleteBookDetails(0L);
		assertThat(result).isNotNull();
	}
	
	@Test
	void findBookDetailsByISBNTest1() {
		Long isbn = 1231231231L;
		BookDetails bookDetails = entityGenerator.getMockBookDetails();
		bookDetails.setIsbn(isbn);
		when(repo.findByIsbn(isbn)).thenReturn(Optional.of(bookDetails));
		BookDetails result = service.findBookDetailsByISBN(isbn);
		assertThat(result.getIsbn()).isEqualTo(isbn);
	}

	@Test
	void findBookDetailsByISBNTest2() {
		Long isbn = 1231231231L;
		when(repo.findByIsbn(any(Long.class))).thenReturn(Optional.empty());
		BookDetails result = service.findBookDetailsByISBN(isbn);
		assertThat(result).isNull();
	}


	@Test
	void findBookDetailsByIdTest1() {
		Optional<BookDetails> bookDetails= Optional.of(entityGenerator.getMockBookDetails());
		bookDetails.get().setBookDetailsId(0L);
		when(repo.findById(any(Long.class))).thenReturn(bookDetails);
		BookDetails result = service.findBookDetailsById(0L);
		assertThat(result.getBookDetailsId()).isEqualTo(0L);
	}

	@Test
	void findBookDetailsByIdTest2() {
		when(repo.findById(0L)).thenReturn(Optional.empty());
		BookDetails result = service.findBookDetailsById(0L);
		assertThat(result).isNull();
	}

	@Test
	void setAvailableCopiesTest1() {
		BookDetails bookDetails= entityGenerator.getMockBookDetails();
		when(repo.save(any(BookDetails.class))).thenReturn(bookDetails);
		service.setAvailableCopies(bookDetails,"checkOut");
		verify(repo, times(1)).save(bookDetails);
		
	}
	@Test
	void setAvailableCopiesTest2() {
		BookDetails bookDetails= entityGenerator.getMockBookDetails();
		when(repo.save(any(BookDetails.class))).thenReturn(bookDetails);
		service.setAvailableCopies(bookDetails,"checkIn");
		verify(repo, times(1)).save(bookDetails);
	}
	
}
