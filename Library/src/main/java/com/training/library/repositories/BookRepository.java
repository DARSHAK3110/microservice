package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.request.BookDto;
import com.training.library.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findByIsbn(Long number);

	@Query(value = "select new com.training.library.dto.request.BookDto(isbn,title, publicationDate, author.authorId) from Book where upload.uploadId= :uploadId")
	Page<BookDto> findBooksByUploadId(Long uploadId, Pageable pageble);
}
