package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.library.models.Borrowing;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long>{

}
