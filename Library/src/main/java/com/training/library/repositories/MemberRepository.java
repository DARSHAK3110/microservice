package com.training.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.library.dto.request.MemberDto;
import com.training.library.models.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	@Query(value = "select new com.training.library.dto.request.MemberDto(name, email, phone) from Member where upload.uploadId= :uploadId")
	Page<MemberDto> findMembersByUploadId(Long uploadId, Pageable pageble);
}
