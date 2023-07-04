package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Genres;

public interface GenreRepository extends JpaRepository<Genres, Long> {
	Optional<Genres> findByGenreName(String name);
}
