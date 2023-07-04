package com.training.library.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Books;

public interface BookRepository extends JpaRepository<Books, Long> {
	
	Optional<Books> findByIsbn(Long number);
}
