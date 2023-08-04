package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.LocationResponseDto;
import com.training.library.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
	@Query(value = "select  new com.training.library.dto.response.LocationResponseDto(locationId,position,shelf.shelfId,shelf.shelfNo,shelf.section.sectionId,shelf.section.sectionName,shelf.section.floor.floorId,shelf.section.floor.floorNo) from Location where deletedAt is null and locationId = :id")
	Optional<LocationResponseDto> findByLocationIdAndDeletedAtIsNull(Long id);

	@Query(value = "select  new com.training.library.dto.response.LocationResponseDto(locationId,position,shelf.shelfId,shelf.shelfNo,shelf.section.sectionId,shelf.section.sectionName, shelf.section.floor.floorId,shelf.section.floor.floorNo) from Location where deletedAt is null and shelf.section.sectionName like %:search% or shelf.section.floor.floorNo like %:search% or shelf.shelfNo like %:search% or position like %:search%")
	Page<LocationResponseDto> findAllByDeletedAtIsNull(String search, Pageable pageable);

	@Modifying
	@Query(value = "update location set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByLocationId(Long id);

	@Query(value = "select  new com.training.library.dto.response.LocationResponseDto(locationId,position,shelf.shelfId,shelf.shelfNo,shelf.section.sectionId,shelf.section.sectionName, shelf.section.floor.floorId,shelf.section.floor.floorNo) from Location where deletedAt is null and shelf.shelfId= :shelfId and isAvailable = TRUE")
	List<LocationResponseDto> getAllByShelf_ShelfIdAndDeletedAtIsNotNull(Long shelfId);
}
