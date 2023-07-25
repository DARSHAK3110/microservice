package com.training.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.authentication.entity.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

}
