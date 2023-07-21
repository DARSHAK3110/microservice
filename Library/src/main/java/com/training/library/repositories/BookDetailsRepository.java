package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.view.BookDetailsView;
import com.training.library.entity.BookDetails;

public interface BookDetailsRepository extends JpaRepository<BookDetails , Long>{

//	@Query(value = "select new com.training.library.dto.response.BookDetailsResponseDto(bookDetailsId , title, isbn, totalCopies, availableCopies, author.authorName) from BookDetails where deletedAt is null and bookDetailsId = :id")
//	Optional<BookDetailsResponseDto> findByBookDetailsIdAndDeletedAtIsNull(Long id);

	/*
	 * @Query(value =
	 * "select new com.training.library.dto.response.BookDetailsResponseDto(bookDetailsId,title, isbn, totalCopies, availableCopies, author.authorName, bookStatus) from BookDetails where deletedAt is null"
	 * ) Optional<List<BookDetailsResponseDto>> findAllByDeletedAtIsNull();
	 */

	Optional<BookDetailsView> findByBookDetailsIdAndDeletedAtIsNull(Long id);

	Optional<List<BookDetailsView>> findAllByDeletedAtIsNull();

	@Modifying
	@Query(value = "update book_details set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByBookDetailsId(Long id);

	
	Optional<BookDetails> findByIsbn(Long bookDetailsId);

	
}
