package com.training.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.request.ReservationDto;
import com.training.library.models.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	@Query(value = "select new com.training.library.dto.request.ReservationDto(book.isbn, member.memberId, reservationDate) from Reservation where upload.uploadId= :uploadId")
	Page<ReservationDto> findReservationsByUploadId(Long uploadId, Pageable pageble);
}
