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

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Location;
import com.training.library.entity.Upload;
import com.training.library.entity.User;
import com.training.library.repositories.BookDetailsRepository;
import com.training.library.repositories.BookStatusRepository;
import com.training.library.repositories.UploadRepository;

@Service
@PropertySource("classpath:message.properties")
public class BookDetailsService {

	@Autowired
	private BookDetailsRepository bookDetailsRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private BookStatusRepository bookStatusRepository;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private UploadRepository uploadRepository;
	@Autowired
	private LocationService locationService;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;

	public BookDetailsResponseDto findBookDetails(Long id) {
		Optional<BookDetailsResponseDto> bookDetails = bookDetailsRepository.findByBookDetailsIdAndDeletedAtIsNull(id);
		if (bookDetails.isPresent()) {
			return bookDetails.get();
		}
		return null;
	}

	public Page<BookDetailsResponseDto> findAllBookDetails(FilterDto dto) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return bookDetailsRepository
				.findAllByDeletedAtIsNullAndTitleIgnoreCaseContainingOrAuthor_AuthorNameIgnoreCaseContaining(
						dto.getSearch(), dto.getSearch(), pageable);
	}

	public ResponseEntity<CustomBaseResponseDto> saveBookDetails(BookDetailsRequestDto dto, String userName) {
		BookDetails bookDetails = new BookDetails();
		User user = userService.findByPhone(Long.parseLong(userName));
		Upload upload = new Upload();
		if (user == null) {
			user = userService.newUser(userName);
		}
		upload.setUser(user);

		bookDetails.setAvailableCopies(0L);
		bookDetails.setTitle(dto.getTitle());
		bookDetails.setIsbn(dto.getIsbn());
		bookDetails.setTotalCopies(0L);
		bookDetails.setAuthor(authorService.findAuthorByAuthorId(dto.getAuthorId()));
		upload.addBookDetails(bookDetails);

		uploadRepository.save(upload);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public ResponseEntity<CustomBaseResponseDto> updateBookDetails(Long id, BookDetailsRequestDto dto, String userName) {
		Upload upload = new Upload();
		BookStatus bs = null;
		Optional<BookDetails> bookDetailsOptional = bookDetailsRepository.findById(id);
		User user = userService.findByPhone(Long.parseLong(userName));
		if (bookDetailsOptional.isPresent()) {
			BookDetails bookDetails = bookDetailsOptional.get();
			if (dto.getBookStatus() != null) {
				Optional<BookStatus> bookStatus = bookStatusRepository.findById(dto.getBookStatus().getBookStatusId());
				if (bookStatus.isEmpty()) {
					bs = new BookStatus();
					bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() + 1L);
					bookDetails.setTotalCopies(bookDetails.getTotalCopies() + 1L);
					bs.setUpload(upload);
				} else {
					bs = bookStatus.get();
					Location locationOld = locationService.findLocationById(bs.getLocation().getLocationId());
					locationOld.setIsAvailable(true);
					locationService.updateLocationAvailability(locationOld);
				}
				bs.setAvailable(true);

				Location location = locationService.findLocationById(dto.getBookStatus().getLocationId());
				location.setIsAvailable(false);
				locationService.updateLocationAvailability(location);
				bs.setLocation(locationService.findLocationById(dto.getBookStatus().getLocationId()));
				bookDetails.addBookStatus(bs);
			}
			bookDetails.setTitle(dto.getTitle());
			bookDetails.setAuthor(authorService.findAuthorByAuthorId(dto.getAuthorId()));
			upload.addBookDetails(bookDetails);

			if (user == null) {
				user = userService.newUser(userName);
			}
			upload.setUser(user);
			upload.setUser(user);

			uploadRepository.save(upload);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public ResponseEntity<CustomBaseResponseDto> deleteBookDetails(Long id) {
		bookDetailsRepository.deleteByBookDetailsId(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public BookDetails findBookDetailsById(Long bookDetailsId) {
		Optional<BookDetails> bookDetails = bookDetailsRepository.findById(bookDetailsId);
		if (bookDetails.isPresent()) {
			return bookDetails.get();
		}
		return null;
	}

	public BookDetails findBookDetailsByISBN(Long bookDetailsId) {
		Optional<BookDetails> bookDetails = bookDetailsRepository.findByIsbn(bookDetailsId);
		if (bookDetails.isPresent()) {
			return bookDetails.get();
		}
		return null;
	}

	public void setAvailableCopies(BookDetails bookDetails, String string) {
		if (string.equals("checkOut")) {
			bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() + 1L);
		} else {
			bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() - 1L);
		}
		bookDetailsRepository.save(bookDetails);
	}
}
