package com.training.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.request.LibrariranDto;
import com.training.library.models.Librarian;

public interface LibrariansRepository extends JpaRepository<Librarian, Long>{

	@Query(value = "select new com.training.library.dto.request.LibrariranDto(name, email, phone) from Librarian where upload.uploadId= :uploadId")
	Page<LibrariranDto> findLibrariansByUploadId(Long uploadId, Pageable pageble);

}
