package com.training.library.services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.emails.EmailSender;
import com.training.library.entity.BookBorrowing;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.entity.User;
import com.training.library.repositories.BookBorrowingRepository;

import jakarta.transaction.Transactional;

@Service
@PropertySource("classpath:message.properties")
public class BookBorrowingService {

	@Autowired
	private EmailSender emailSender;
	@Autowired
	private BookBorrowingRepository bookBorrowingRepository;
	@Autowired
	private BookStatusService bookStatusService;
	@Autowired
	private BookDetailsService bookDetailService;
	@Autowired
	private BookReservationService bookReservationService;
	@Autowired
	private UserService userService;
	@Autowired
	private RestTemplate restTemplate;
	private static final String OPERATION_SUCCESS = "operation.success";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	@Autowired
	private Environment env;

	public BookBorrowingResponseDto findBookBorrowing(Long id) {
		Optional<BookBorrowingResponseDto> bookBorrowing = bookBorrowingRepository
				.findByBookBorrowingIdAndDeletedAtIsNull(id);
		if (bookBorrowing.isPresent()) {
			return bookBorrowing.get();
		}
		return null;
	}

	public Page<BookBorrowingResponseDto> findAllBookBorrowing(FilterDto dto, String userName) throws ParseException {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		if (dto.getStartDate() == null) {
			dto.setStartDate("2020-01-01");
		}
		if (dto.getEndDate() == null) {
			dto.setEndDate(LocalDate.now().plusDays(3).toString());
		}
		if (!dto.isDeletedAt()) {
			if (dto.isUser()) {
				return bookBorrowingRepository.findAllwithSearchByBorrowingDate(dto.getSearch(), pageble, userName,
						new SimpleDateFormat(DATE_FORMAT).parse(dto.getStartDate()),
						new SimpleDateFormat(DATE_FORMAT).parse(dto.getEndDate()));
			}
			return bookBorrowingRepository.findAllwithSearchBorrowingDate(dto.getSearch(), pageble,
					new SimpleDateFormat(DATE_FORMAT).parse(dto.getStartDate()),
					new SimpleDateFormat(DATE_FORMAT).parse(dto.getEndDate()));
		} else {
			if (dto.isUser()) {
				return bookBorrowingRepository.findAllwithSearchByReturnDate(dto.getSearch(), pageble, userName,
						new SimpleDateFormat(DATE_FORMAT).parse(dto.getStartDate()),
						new SimpleDateFormat(DATE_FORMAT).parse(dto.getEndDate()));
			}
			return bookBorrowingRepository.findAllwithSearchReturnDate(dto.getSearch(), pageble,
					new SimpleDateFormat(DATE_FORMAT).parse(dto.getStartDate()),
					new SimpleDateFormat(DATE_FORMAT).parse(dto.getEndDate()));
		}
	}

	public ResponseEntity<CustomBaseResponseDto> saveBookBorrowing(BookBorrowingRequestDto dto, String userName) {
		User borrower = userService.findByPhone(dto.getPhone());
		BookStatus bookStatus = bookStatusService.findBookById(dto.getBookStatusId());
		bookStatus.setAvailable(false);
		bookStatus.setReserved(false);
		BookBorrowing br = new BookBorrowing();
		if (borrower == null) {
			borrower = userService.newUser(dto.getPhone().toString());
		}
		br.setBorrower(borrower);
		br.setBookStatus(bookStatus);
		BookDetails bookDetails = bookStatus.getBookDetails();
		bookDetailService.setAvailableCopies(bookDetails, "checkIn");
		User user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		br.setUser(user);
		bookStatusService.updateBookStatusAvailability(bookStatus);
		bookBorrowingRepository.save(br);
		if (dto.getIsReserved()) {
			bookReservationService.setReservationFinished(dto.getPhone(), dto.getBookStatusId());
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteBookBorrowing(Long id) {
		BookBorrowingResponseDto bookBorrowing = null;
		Optional<BookBorrowingResponseDto> bookBorrowingOptional = bookBorrowingRepository
				.findByBookBorrowingIdAndDeletedAtIsNull(id);
		if (bookBorrowingOptional.isPresent()) {
			bookBorrowing = bookBorrowingOptional.get();
			BookStatus bookStatus = this.bookStatusService.findBookById(bookBorrowing.getBookId());
			bookStatus.setAvailable(true);
			BookDetails bookDetails = bookStatus.getBookDetails();
			bookDetailService.setAvailableCopies(bookDetails, "checkOut");
			this.bookStatusService.updateBookStatusAvailability(bookStatus);
			bookBorrowingRepository.deleteByBookBorrowingId(id);
		}

		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public BookBorrowingResponseDto findBookBorrowingByBookStatus(Long id) {
		Optional<BookBorrowingResponseDto> bookBorrowing = bookBorrowingRepository
				.findByBookStatus_BookStatusIdAndDeletedAtIsNull(id);
		if (bookBorrowing.isPresent()) {
			return bookBorrowing.get();
		}
		return null;
	}

	public Boolean countBookBorrowingByUserPhone(Long id) {

		Long counter = bookBorrowingRepository.countByDeletedAtIsNullAndBorrower_Phone(id);
		if (counter < 3) {
			return true;
		}
		return false;
	}

	public Boolean checkUserForDeletion(Long id) {

		Long counter = bookBorrowingRepository.countByDeletedAtIsNullAndBorrower_Phone(id);
		if (counter > 0) {
			return true;
		}
		return false;
	}

	public void sendMailForRememberBeforeExpiration(){
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DATE, -7);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);

		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, -6);
		endDate.set(Calendar.HOUR_OF_DAY, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.SECOND, 0);
		List<BookBorrowing> borrowings = bookBorrowingRepository
				.findAllByCreatedAtBetweenAndDeletedAtIsNull(startDate.getTime(), endDate.getTime());
		borrowings.forEach(b -> {
			String email = restTemplate.getForObject(
					"http://localhost:8090/api/v1/users/email/" + b.getBorrower().getPhone(), String.class);
			String subject = "This is reminder!!!";
			String body = "<div>Today is last day for return the book.</div><div>The book with title: " + "<b>"
					+ b.getBookStatus().getBookDetails().getTitle() + "</b>"
					+ " has been expired today.</div><div>You must return it today Otherwise you will be charged 500$</div>";
			try {
				emailSender.sendSimpleMessage(email, subject, body, false);
			} catch (MessagingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public void sendMailForRememberAfterExpiration() {
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DATE, -7);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);

		List<BookBorrowing> borrowings = bookBorrowingRepository
				.findAllByCreatedAtBeforeAndDeletedAtIsNull(startDate.getTime());
		borrowings.forEach(b -> {
			Calendar expireDate = Calendar.getInstance();
			expireDate.setTime(b.getCreatedAt());
			expireDate.add(Calendar.DATE, +7);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String email = restTemplate.getForObject(
					"http://localhost:8090/api/v1/users/email/" + b.getBorrower().getPhone(), String.class);
			String subject = "This is reminder!!! You already lost the date for book "
					+ b.getBookStatus().getBookDetails().getTitle();
			String body = "<div><span style='color:red'>" + formatter.format(expireDate.getTime())
					+ "</span> was last day for return the book.</div><div>The book with title:" + "<b>"
					+ b.getBookStatus().getBookDetails().getTitle() + "</b>" + "had been expired already. </div>"
					+ "<div><span style='color:red'>*You must return it today and you have to pay 500$</span></div>";
			try {
				emailSender.sendSimpleMessage(email, subject, body, false);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}
}