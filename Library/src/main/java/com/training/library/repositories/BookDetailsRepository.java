package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.entity.BookDetails;

public interface BookDetailsRepository extends JpaRepository<BookDetails , Long>{

	@Query(value =
			  "select new com.training.library.dto.response.BookDetailsResponseDto(bookDetailsId,title, isbn, author.authorName, author.authorId,totalCopies, availableCopies) from BookDetails where deletedAt is null and bookDetailsId = :id")
	Optional<BookDetailsResponseDto> findByBookDetailsIdAndDeletedAtIsNull(Long id);

	@Query(value =
			  "select new com.training.library.dto.response.BookDetailsResponseDto(bookDetailsId,title, isbn, author.authorName, author.authorId,totalCopies, availableCopies) from BookDetails where deletedAt is null and (title like %:title% or author.authorName like %:authorName%)")
	Page<BookDetailsResponseDto> findAllByDeletedAtIsNullAndTitleIgnoreCaseContainingOrAuthor_AuthorNameIgnoreCaseContaining(String title, String authorName,Pageable pageable);

	@Modifying
	@Query(value = "update book_details set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByBookDetailsId(Long id);

	Optional<BookDetails> findByIsbn(Long isbn);

	
	@Query(value="select count(*) from BookDetails bd left join BookStatus bs on bd.bookDetailsId = bs.bookDetails.bookDetailsId where bs.isAvailable = FALSE and bs.deletedAt is null and bd.bookDetailsId = :id ")
	Long countBookNotAvailable(Long id);

	Long countByDeletedAtIsNullAndBookDetailsIdAndBookStatus_IsAvailableTrueAndBookStatus_IsReservedIsFalse(
			Long bookId);

	  
}