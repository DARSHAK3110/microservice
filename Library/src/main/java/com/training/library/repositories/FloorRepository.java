package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.FloorResponseDto;
import com.training.library.entity.Floor;

public interface FloorRepository extends JpaRepository<Floor, Long> {

	@Query(value = "select new com.training.library.dto.response.FloorResponseDto(floorId,floorNo) from Floor where floorId = :id and deletedAt is null")
	Optional<FloorResponseDto> findByFloorIdAndDeletedAtIsNull(Long id);

	@Query(value = "select new com.training.library.dto.response.FloorResponseDto(floorId,floorNo) from Floor where deletedAt is null and floorNo like %:search%")
	Page<FloorResponseDto> findAllByDeletedAtIsNull(String search, Pageable pageble);

	Optional<Floor> findByFloorNo(Long floorNo);

	Long countByFloorNo(Long floorNo);

}
