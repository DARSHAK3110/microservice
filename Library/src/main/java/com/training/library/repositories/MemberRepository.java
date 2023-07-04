package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.library.models.Members;

public interface MemberRepository extends JpaRepository<Members, Long>{
	Optional<Members> findByEmail(String email);
}
