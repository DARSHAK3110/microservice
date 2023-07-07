package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Librarian;

public interface LibrariansRepository extends JpaRepository<Librarian, Long>{

}
