package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{

	@Query(value = "select new com.training.library.dto.response.AuthorResponseDto(authorId,authorName, authorDOB) from Author where authorId = :id and deletedAt is null")
	Optional<AuthorResponseDto> findByAuthorIdAndDeletedAtIsNull(Long id);

	@Query(value = "select new com.training.library.dto.response.AuthorResponseDto(authorId,authorName, authorDOB) from Author where deletedAt is null")
	Optional<List<AuthorResponseDto>> findAllByDeletedAtIsNull();

	@Modifying
	@Query(value = "update author set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByAuthorId(Long id);
}
