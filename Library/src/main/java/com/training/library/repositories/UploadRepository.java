package com.training.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.library.models.Upload;

public interface UploadRepository extends JpaRepository<Upload, Long>{

}
