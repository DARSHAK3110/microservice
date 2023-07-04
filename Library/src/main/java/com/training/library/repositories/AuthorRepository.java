package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
