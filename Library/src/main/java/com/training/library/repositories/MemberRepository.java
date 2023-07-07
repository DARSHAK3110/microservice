package com.training.library.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.training.library.models.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
}
