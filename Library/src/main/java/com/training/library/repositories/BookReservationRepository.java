package com.training.library.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.entity.BookReservation;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long>{
	
	@Query(value = "select new com.training.library.dto.response.BookReservationResponseDto(bookReservationId, reserver.userId, bookDetails.title, bookDetails.id, reserver.phone, reservationDate,bookDetails.isbn, isAccepted) from BookReservation where deletedAt is null and bookReservationId = :id")
	Optional<BookReservationResponseDto> findByBookReservationIdAndDeletedAtIsNull(Long id);
	

	@Query(value = "select new com.training.library.dto.response.BookReservationResponseDto(bookReservationId, reserver.userId, bookDetails.title, bookDetails.id, reserver.phone, reservationDate,bookDetails.isbn, isAccepted, bookDetails.totalCopies, bookDetails.availableCopies) from BookReservation where bookDetails.title like %:search% or reserver.phone like %:search% order by createdAt desc")
	Page<BookReservationResponseDto> findAllByDeletedAtIsNull(String search, Pageable pageable);


	@Query(value = "select new com.training.library.dto.response.BookReservationResponseDto(bookReservationId, reserver.userId, bookDetails.title, bookDetails.id, reserver.phone, reservationDate,bookDetails.isbn, isAccepted) from BookReservation where reserver.phone = :userId and(bookDetails.title like %:search% or reserver.phone like %:search%) order by createdAt desc")
	Page<BookReservationResponseDto> findAllByDeletedAtIsNull(String search, Pageable pageable, Long userId);

	
	@Modifying
	@Query(value = "update book_reservation set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByBookReservationId(Long id);


	@Modifying
	  @Query(value = "update book_reservation set deleted_at = now(), is_accepted = FALSE where reservation_date < :expiryDate", nativeQuery = true)
	    public void deleteAllByReservationDateBefore(Date expiryDate);
	
	Optional<BookReservation> findByBookDetails_BookDetailsIdAndReserver_PhoneAndDeletedAtIsNull(Long bookDetailsId,
			long parseLong);

	Long countByBookDetails_BookDetailsIdAndDeletedAtIsNull(Long bookDetailsId);

	Long countByBookDetails_BookDetailsIdAndDeletedAtIsNullAndIsAcceptedTrue(Long bookDetailsId);
}




