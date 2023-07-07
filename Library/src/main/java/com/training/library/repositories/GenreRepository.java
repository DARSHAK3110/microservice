package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
	Optional<Genre> findByGenreName(String name);
}
