package com.training.library.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.request.BookDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Location;
import com.training.library.entity.Upload;
import com.training.library.entity.User;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.repositories.BookDetailsRepository;
import com.training.library.repositories.UploadRepository;

@Service
@PropertySource("classpath:message.properties")
public class BookDetailsService {

	@Autowired
	private BookDetailsRepository bookDetailsRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private BookStatusService bookStatusService;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private CartService cartService;
	@Autowired
	private BookReservationService reserveService;
	@Autowired
	private UploadRepository uploadRepository;
	@Autowired
	private LocationService locationService;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	@Autowired
	private DtoToEntity dtoToEntity;

	public BookDetailsResponseDto findBookDetails(Long id) {
		Optional<BookDetailsResponseDto> bookDetails = bookDetailsRepository.findByBookDetailsIdAndDeletedAtIsNull(id);
		if (bookDetails.isPresent()) {
			return bookDetails.get();
		}
		return null;
	}

	public Page<BookDetailsResponseDto> findAllBookDetails(FilterDto dto, String userName) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		Page<BookDetailsResponseDto> result = bookDetailsRepository
				.findAllByDeletedAtIsNullAndTitleIgnoreCaseContainingOrAuthor_AuthorNameIgnoreCaseContaining(
						dto.getSearch(), dto.getSearch(), pageable);
		if (dto.isUser()) {
			List<BookDetailsResponseDto> content = result.getContent();
			for (BookDetailsResponseDto response : content) {
				if (checkInCart(response.getBookDetailsId(), userName)) {
					response.setAddedToCart(true);
				} else {
					response.setAddedToCart(false);
				}
				if (isReserved(response.getBookDetailsId(), userName)) {
					response.setReserved(true);
				} else {
					response.setReserved(false);
				}
					
			}
			return PageableExecutionUtils.getPage(content, pageable, () -> result.getTotalElements());
		}
		return result;
	}

	private boolean isReserved(Long bookDetailsId, String userName) {
		return reserveService.checkReservation(bookDetailsId, userName);
	}

	private boolean checkInCart(Long bookDetailsId, String userName) {
		return cartService.checkCart(bookDetailsId, userName);
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

	public ResponseEntity<CustomBaseResponseDto> updateBookDetails(Long id, BookDetailsRequestDto dto,
			String userName) {
		Upload upload = new Upload();
		BookStatus bs = null;
		Optional<BookDetails> bookDetailsOptional = bookDetailsRepository.findById(id);
		User user = userService.findByPhone(Long.parseLong(userName));
		if (bookDetailsOptional.isPresent()) {
			BookDetails bookDetails = bookDetailsOptional.get();
			if (dto.getBookStatus() != null) {
				bs = bookStatusService.findBookById(dto.getBookStatus().getBookStatusId());
				if (bs == null) {
					bs = new BookStatus();
					bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() + 1L);
					bookDetails.setTotalCopies(bookDetails.getTotalCopies() + 1L);
					bs.setUpload(upload);
				} else {
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

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteBookDetails(Long id) {

		Long counter = bookDetailsRepository.countBookNotAvailable(id);
		if (counter == 0) {
			BookDetails bookDetails = findBookDetailsById(id);
			List<BookStatus> bookStatus = bookDetails.getBookStatus();
			for (BookStatus bs : bookStatus) {
				bookStatusService.deleteBookStatus(bs);
			}
			bookDetailsRepository.deleteByBookDetailsId(id);
		} else {
			throw new RuntimeException("For delete book, book really required at location");
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public BookDetails findBookDetailsById(Long bookDetailsId) {
		Optional<BookDetails> bookDetails = bookDetailsRepository.findById(bookDetailsId);
		if (bookDetails.isPresent()) {
			return bookDetails.get();
		}
		return null;
	}

	public BookDetails findBookDetailsByISBN(Long isbn) {
		Optional<BookDetails> bookDetails = bookDetailsRepository.findByIsbn(isbn);
		if (bookDetails.isPresent()) {
			return bookDetails.get();
		}
		return null;
	}

	public void setAvailableCopies(BookDetails bookDetails, String string) {
		if (string.equals("checkOut")) {
			bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() + 1L);
		} else if (string.equals("checkIn")) {
			bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() - 1L);
		} else if (string.equals("remove")) {
			bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() - 1L);
			bookDetails.setTotalCopies(bookDetails.getTotalCopies() - 1L);
		}
		bookDetailsRepository.save(bookDetails);
	}

	public ResponseEntity<CustomBaseResponseDto> uploadBooks(Long isbn, MultipartFile file, String userName)
			throws CustomExceptionHandler, IOException, ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException,
			SecurityException, NoSuchFieldException {
		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<BookDto> booksDto = mapper.mapToList(BookDto.class);
		List<BookStatus> books = dtoToEntity.toBookStatus(booksDto);
		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		User user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		upload.setUser(user);
		BookDetails bookDetails = findBookDetailsByISBN(isbn);
		if (bookDetails != null) {
			for (BookStatus book : books) {
				book.setAvailable(true);
				book.setUpload(upload);
				bookDetails.addBookStatus(book);
				bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() + 1L);
				bookDetails.setTotalCopies(bookDetails.getTotalCopies() + 1L);
				Location location = book.getLocation();
				location.setIsAvailable(false);
				locationService.updateLocationAvailability(location);
			}
		}

		upload.addAllBookDetails(List.of(bookDetails));
		uploadRepository.save(upload);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

}
