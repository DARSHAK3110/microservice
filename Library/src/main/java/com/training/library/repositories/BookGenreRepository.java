package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.library.models.BooksGenre;

public interface BookGenreRepository extends JpaRepository<BooksGenre, Long> {

}
