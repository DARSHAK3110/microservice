package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.library.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByPhone(Long phone);
}
