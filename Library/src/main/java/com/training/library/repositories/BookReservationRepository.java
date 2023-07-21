package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.entity.BookReservation;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long>{
	
	@Query(value = "select new com.training.library.dto.response.BookReservationResponseDto(bookReservationId, reserver.userId, bookDetails.title, bookDetails.id, reserver.phone, reservationDate,bookDetails.isbn) from BookReservation where deletedAt is null and bookReservationId = :id")
	Optional<BookReservationResponseDto> findByBookReservationIdAndDeletedAtIsNull(Long id);
	

	@Query(value = "select new com.training.library.dto.response.BookReservationResponseDto(bookReservationId, reserver.userId, bookDetails.title, bookDetails.id, reserver.phone, reservationDate,bookDetails.isbn) from BookReservation where deletedAt is null")
	Optional<List<BookReservationResponseDto>> findAllByDeletedAtIsNull();


	@Modifying
	@Query(value = "update book_reservation set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByBookReservationId(Long id);
}
