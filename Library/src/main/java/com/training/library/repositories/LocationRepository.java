package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.LocationResponseDto;
import com.training.library.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
	@Query(value = "select  new com.training.library.dto.response.LocationResponseDto(locationId,position,new com.training.library.dto.response.ShelfResponseDto(shelf.shelfId,shelf.shelfNo,new com.training.library.dto.response.SectionResponseDto(shelf.section.sectionId,shelf.section.sectionName, new com.training.library.dto.response.FloorResponseDto(shelf.section.floor.floorId,shelf.section.floor.floorNo)) )) from Location where deletedAt is null and locationId = :id")
	Optional<LocationResponseDto> findByLocationIdAndDeletedAtIsNull(Long id);

	@Query(value = "select  new com.training.library.dto.response.LocationResponseDto(locationId,position,new com.training.library.dto.response.ShelfResponseDto(shelf.shelfId,shelf.shelfNo,new com.training.library.dto.response.SectionResponseDto(shelf.section.sectionId,shelf.section.sectionName, new com.training.library.dto.response.FloorResponseDto(shelf.section.floor.floorId,shelf.section.floor.floorNo)) )) from Location where deletedAt is null")
	Optional<List<LocationResponseDto>> findAllByDeletedAtIsNull();

	@Modifying
	@Query(value = "update location set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByLocationId(Long id);
}
