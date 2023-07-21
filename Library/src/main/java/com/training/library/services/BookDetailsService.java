package com.training.library.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.view.BookDetailsView;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Upload;
import com.training.library.entity.User;
import com.training.library.repositories.BookDetailsRepository;
import com.training.library.repositories.BookStatusRepository;
import com.training.library.repositories.UploadRepository;
import com.training.library.repositories.UserRepository;

@Service
public class BookDetailsService {

	@Autowired
	private BookDetailsRepository bookDetailsRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookStatusRepository bookStatusRepository;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private UploadRepository uploadRepository;
	@Autowired
	private LocationService locationService;

	public BookDetailsView findBookDetails(Long id) {
		Optional<BookDetailsView> bookDetails = bookDetailsRepository.findByBookDetailsIdAndDeletedAtIsNull(id);
		if (bookDetails.isPresent()) {
			return bookDetails.get();
		}
		return null;
	}

	public List<BookDetailsView> findAllBookDetails() {
		Optional<List<BookDetailsView>> bookDetailsList = bookDetailsRepository.findAllByDeletedAtIsNull();
		if (bookDetailsList.isPresent()) {
			return bookDetailsList.get();
		}
		return Collections.emptyList();
	}

	public List<BookDetailsView> saveBookDetails(BookDetailsRequestDto dto, String userName) {
		BookDetails bookDetails = new BookDetails();
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong(userName));
		User user = null;
		Upload upload = new Upload();
		if (userOptional.isEmpty()) {
			User newUser = new User();
			newUser.setPhone(Long.parseLong(userName));
			user = userRepository.save(newUser);
		} else {
			user = userOptional.get();
		}
		upload.setUser(user);

		bookDetails.setAvailableCopies(0L);
		bookDetails.setTitle(dto.getTitle());
		bookDetails.setIsbn(dto.getIsbn());
		bookDetails.setTotalCopies(0L);
		bookDetails.setAuthor(authorService.findAuthorByAuthorId(dto.getAuthorId()));
		upload.addBookDetails(bookDetails);

		uploadRepository.save(upload);
		return findAllBookDetails();
	}

	public List<BookDetailsView> updateBookDetails(Long id, BookDetailsRequestDto dto, String userName) {
		Upload upload = new Upload();
		BookStatus bs = null;
		User user = null;
		Optional<BookDetails> bookDetailsOptional = bookDetailsRepository.findById(id);
		Optional<User> userOptional = userRepository.findByPhone(Long.parseLong(userName));

		if (bookDetailsOptional.isPresent()) {
			Optional<BookStatus> bookStatus = bookStatusRepository.findById(dto.getBookStatus().getBookStatusId());
			BookDetails bookDetails = bookDetailsOptional.get();

			if (bookStatus.isEmpty()) {
				bs = new BookStatus();
				bs.setUpload(upload);
			} else {
				bs = bookStatus.get();
			}
			bs.setAvailable(true);
			bs.setLocation(locationService.findLocationById(dto.getBookStatus().getLocation()));
			bookDetails.addBookStatus(bs);

			bookDetails.setTitle(dto.getTitle());
			bookDetails.setAvailableCopies(bookDetails.getAvailableCopies() + 1L);
			bookDetails.setTotalCopies(bookDetails.getTotalCopies() + 1L);
			bookDetails.setAuthor(authorService.findAuthorByAuthorId(dto.getAuthorId()));
			upload.addBookDetails(bookDetails);

			if (userOptional.isEmpty()) {
				User newUser = new User();
				newUser.setPhone(Long.parseLong(userName));
				user = userRepository.save(newUser);
			} else {
				user = userOptional.get();
			}
			upload.setUser(user);

			uploadRepository.save(upload);
		}
		return findAllBookDetails();
	}

	public List<BookDetailsView> deleteBookDetails(Long id) {
		bookDetailsRepository.deleteByBookDetailsId(id);
		return findAllBookDetails();
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
}
