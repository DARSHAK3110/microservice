package com.training.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.request.AuthorDto;
import com.training.library.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	@Query(value = "select new com.training.library.dto.request.AuthorDto(authorName) from Author where upload.uploadId= :uploadId")
	Page<AuthorDto> findAuthorsByUploadId(Long uploadId, Pageable pageble);
}
