package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.SectionResponseDto;
import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.entity.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
	@Query(value = "select new com.training.library.dto.response.ShelfResponseDto(shelfId,shelfNo,section.sectionId,section.sectionName,section.floor.floorNo,section.floor.floorId) from Shelf where deletedAt is null and shelfId = :id")
	Optional<ShelfResponseDto> findByShelfIdAndDeletedAtIsNull(Long id);

	@Query(value = "select new com.training.library.dto.response.ShelfResponseDto(shelfId,shelfNo,section.sectionId,section.sectionName,section.floor.floorNo,section.floor.floorId) from Shelf where deletedAt is null and section.sectionName like %:search% or section.floor.floorNo like %:search% or shelfNo like %:search%")
	Page<ShelfResponseDto> findAllByDeletedAtIsNull(String search, Pageable pageble);

	@Modifying
	@Query(value = "update shelf set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByShelfId(Long id);

	@Query(value = "select new com.training.library.dto.response.ShelfResponseDto(shelfId, shelfNo,section.sectionId,section.sectionName,section.floor.floorId,section.floor.floorNo) from Shelf where deletedAt is null and section.sectionId = :sectionId")
	List<ShelfResponseDto> getAllBySection_SectionIdAndDeletedAtIsNotNull(Long sectionId);
}
