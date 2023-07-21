package com.training.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.view.BookStatusView;
import com.training.library.entity.BookStatus;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long>{
	Optional<BookStatusView> findByBookStatusIdAndDeletedAtIsNull(Long id);

	Optional<List<BookStatusView>> findAllByDeletedAtIsNull();

	@Modifying
	@Query(value = "update book_status set deleted_at = now() where id= :id", nativeQuery = true)
	void deleteByBookStatusId(Long id);
	
}
