package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.SectionResponseDto;
import com.training.library.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long>  {

	@Query(value = "select new com.training.library.dto.response.SectionResponseDto(sectionId,sectionName,floor.floorId,floor.floorNo) from Section where deletedAt is null and sectionId = :id")
	Optional<SectionResponseDto> findBySectionIdAndDeletedAtIsNull(Long id);

	@Query(value = "select new com.training.library.dto.response.SectionResponseDto(sectionId,sectionName,floor.floorId,floor.floorNo) from Section where deletedAt is null and floor.floorNo like %:search% or  sectionName like %:search% ")
	Page<SectionResponseDto> findAllByDeletedAtIsNull(String search, Pageable pageble);

	@Modifying
	@Query(value = "update section set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteBySectionId(Long id);
	@Query(value = "select new com.training.library.dto.response.SectionResponseDto(sectionId,sectionName,floor.floorId,floor.floorNo) from Section where deletedAt is null and floor.floorId = :floorId")
	List<SectionResponseDto> getAllByFloor_FloorNoAndDeletedAtIsNotNull(Long floorId);
}
