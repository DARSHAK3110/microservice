package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Librarians;

public interface LibrariansRepository extends JpaRepository<Librarians, Long>{

}
