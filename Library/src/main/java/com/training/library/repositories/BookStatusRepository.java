package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.entity.BookStatus;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where bookStatusId = :id and deletedAt is null")
	Optional<BookStatusResponseDto> findByBookStatusIdAndDeletedAtIsNull(Long id);

	@Modifying
	@Query(value = "update book_status set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByBookStatusId(Long id);

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where bookDetails.bookDetailsId = :id and deletedAt is null")
	Page<BookStatusResponseDto> findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(Pageable pageble, Long id);

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where  bookDetails.bookDetailsId = :id and deletedAt is null and isAvailable = :b")
	Page<BookStatusResponseDto> findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(Pageable pageble, Long id,
			boolean b);

	Optional<BookStatus> findByLocation_LocationId(Long locationId);

	Long countByLocation_LocationIdAndDeletedAtIsNull(Long locationId);

	Long countByLocation_Shelf_ShelfIdAndDeletedAtIsNull(Long shelfId);

	Long countByLocation_Shelf_Section_SectionIdAndDeletedAtIsNull(Long sectionId);

	Long countByLocation_Shelf_Section_Floor_FloorIdAndDeletedAtIsNull(Long floorId);

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where location.locationId = :id and deletedAt is null")
	Page<BookStatusResponseDto> findAllByLocationId(Long id, Pageable pageble);

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where location.shelf.shelfId = :id and deletedAt is null")
	Page<BookStatusResponseDto> findAllByShelfId(Long id, Pageable pageble);

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where location.shelf.section.sectionId = :id and deletedAt is null")
	Page<BookStatusResponseDto> findAllBySectionId(Long id, Pageable pageble);

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where location.shelf.section.floor.floorId = :id and deletedAt is null" )
	Page<BookStatusResponseDto> findAllByFloorId(Long id, Pageable pageble);

	@Query(value = "select new com.training.library.dto.response.BookStatusResponseDto(bookStatusId, location.locationId,location.shelf.section.floor.floorId, location.shelf.section.sectionId,location.shelf.shelfId,location.shelf.section.floor.floorNo, location.shelf.section.sectionName, location.shelf.shelfNo, location.position, isAvailable, bookDetails.isbn, bookDetails.bookDetailsId) from BookStatus where  bookDetails.isbn = :id and deletedAt is null and isReserved = FALSE")
	Page<BookStatusResponseDto> findAllByDeletedAtIsNullAndBookDetails_ISBN(Pageable pageble, Long id);
}
