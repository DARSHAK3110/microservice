package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.training.library.entity.User;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	@Autowired
	private EntityGenerator entityGenerator;
	private Long id;

	@BeforeEach
	void setUp() throws Exception {
		User savedUser = repo.save(entityGenerator.getUser());
		id = savedUser.getUserId();
	}

	@Test
	void testFindByPhone() {
		Optional<User> result = repo.findByPhone(1231231231L);
		assertThat(result.get().getUserId()).isEqualTo(id);
	}

}
