package com.training.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.request.ReservationDto;
import com.training.library.models.Borrowing;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long>{

	@Query(value = "select new com.training.library.dto.request.BorrowingDto(book.isbn, member.memberId, borrowingDate, returnDate, dueDate) from Borrowing where upload.uploadId= :uploadId")
	Page<ReservationDto> findBorrowingsByUploadId(Long uploadId, Pageable pageble);

}
