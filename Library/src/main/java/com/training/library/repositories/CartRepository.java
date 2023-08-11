 package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByBookDetails_BookDetailsIdAndUser_Phone(Long bookDetailsId, long parseLong);

	@Query(value =
			  "select new com.training.library.dto.response.BookDetailsResponseDto(bookDetails.bookDetailsId,bookDetails.title, bookDetails.isbn, bookDetails.author.authorName, bookDetails.author.authorId, bookDetails.totalCopies, bookDetails.availableCopies) from Cart where deletedAt is null and user.phone=:userId and (bookDetails.title like %:title% or bookDetails.author.authorName like %:authorName%)")
	Page<BookDetailsResponseDto> findAllByUserId(String title, String authorName, Pageable pageable, Long userId);

	@Modifying
	void deleteByBookDetails_BookDetailsId(Long id);
}
