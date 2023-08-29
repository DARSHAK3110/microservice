package com.training.library.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.entity.BookBorrowing;

public interface BookBorrowingRepository extends JpaRepository<BookBorrowing, Long> {
	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt,bookStatus.bookDetails.title) from BookBorrowing where deletedAt is null and bookBorrowingId = :id")
	Optional<BookBorrowingResponseDto> findByBookBorrowingIdAndDeletedAtIsNull(Long id);
	
	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt,bookStatus.bookDetails.title) from BookBorrowing where (bookStatus.bookDetails.title like %:search% or borrower.phone like %:search%)")
	Page<BookBorrowingResponseDto> findAllwithSearch(String search, Pageable pageble);

	@Modifying
	@Query(value = "update book_borrowing set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByBookBorrowingId(Long id);

	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt, bookStatus.bookDetails.title) from BookBorrowing where deletedAt is null and bookStatus.bookStatusId = :id")
	Optional<BookBorrowingResponseDto> findByBookStatus_BookStatusIdAndDeletedAtIsNull(Long id);

	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt,bookStatus.bookDetails.title) from BookBorrowing where borrower.phone = :userName and (bookStatus.bookDetails.title like %:search% or borrower.phone like %:search%)")
	Page<BookBorrowingResponseDto> findAllwithSearch(String search, Pageable pageble, String userName);

	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt,bookStatus.bookDetails.title) from BookBorrowing where borrower.phone = :userName and (bookStatus.bookDetails.title like %:search% or borrower.phone like %:search%) and createdAt between :startDate and :endDate   order by createdAt desc")
	Page<BookBorrowingResponseDto> findAllwithSearchByBorrowingDate(String search, Pageable pageble, String userName,
			Date startDate, Date endDate);
	
	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt,bookStatus.bookDetails.title) from BookBorrowing where (bookStatus.bookDetails.title like %:search% or borrower.phone like %:search%) and  createdAt between :startDate and :endDate   order by createdAt desc")
	Page<BookBorrowingResponseDto> findAllwithSearchBorrowingDate(String search, Pageable pageble, Date startDate,
			Date endDate);
	
	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt,bookStatus.bookDetails.title) from BookBorrowing where borrower.phone = :userName and (bookStatus.bookDetails.title like %:search% or borrower.phone like %:search%) and deletedAt between :startDate and :endDate   order by createdAt desc")
	Page<BookBorrowingResponseDto> findAllwithSearchByReturnDate(String search, Pageable pageble, String userName,
			Date startDate, Date endDate);
	
	@Query(value = "select new com.training.library.dto.response.BookBorrowingResponseDto(bookBorrowingId, borrower.userId, bookStatus.location.locationId, bookStatus.id, borrower.phone, createdAt, deletedAt,bookStatus.bookDetails.title) from BookBorrowing where (bookStatus.bookDetails.title like %:search% or borrower.phone like %:search%) and deletedAt between :startDate and :endDate   order by createdAt desc")
	Page<BookBorrowingResponseDto> findAllwithSearchReturnDate(String search, Pageable pageble, Date startDate,
			Date endDate);

	Long countByDeletedAtIsNullAndBorrower_Phone(Long id);
	
}
