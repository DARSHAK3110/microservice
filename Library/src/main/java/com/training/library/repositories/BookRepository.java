package com.training.library.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	Optional<Book> findByIsbn(Long number);
}
