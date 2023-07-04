package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.library.models.Borrowings;

public interface BorrowingRepository extends JpaRepository<Borrowings, Long>{

}
