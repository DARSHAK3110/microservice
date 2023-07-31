package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.entity.BookBorrowing;

public interface BookBorrowingRepository extends JpaRepository<BookBorrowing, Long> {
	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, borrowingDate, returnDate,bookStatus.bookDetails.title) from BookBorrowing where deletedAt is null and bookBorrowingId = :id")
	Optional<BookBorrowingResponseDto> findByBookBorrowingIdAndDeletedAtIsNull(Long id);
	

	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, borrowingDate, returnDate,bookStatus.bookDetails.title) from BookBorrowing where bookStatus.bookDetails.title like %:search% or borrower.phone like %:search%")
	Page<BookBorrowingResponseDto> findAllwithSearch(String search, Pageable pageble);


	@Modifying
	@Query(value = "update book_borrowing set deleted_at = now() and return_date = now() where id= :id", nativeQuery = true)
	void deleteByBookBorrowingId(Long id);

	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, borrowingDate, returnDate, bookStatus.bookDetails.title) from BookBorrowing where deletedAt is null and bookStatus.bookStatusId = :id")
	Optional<BookBorrowingResponseDto> findByBookStatus_BookStatusIdAndDeletedAtIsNull(Long id);
}
