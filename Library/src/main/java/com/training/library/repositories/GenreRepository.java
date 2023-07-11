package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.request.GenreDto;
import com.training.library.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
	Optional<Genre> findByGenreName(String name);

	@Query(value = "select new com.training.library.dto.request.GenreDto(genreName) from Genre where upload.uploadId= :uploadId")
	Page<GenreDto> findGenresByUploadId(Long uploadId, Pageable pageble);
}
