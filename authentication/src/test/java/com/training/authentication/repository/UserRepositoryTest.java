package com.training.authentication.repository;

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

import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.User;
import com.training.authentication.entity.enums.Roles;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

	@Autowired
	private UserRepository repo;
	private Long id;

	@BeforeEach
	void setUp() throws Exception {
		User user = new User();
		user.setFirstName("user");
		user.setLastName("demo");
		user.setPassword("123123123");
		user.setPhoneNumber(1231231231L);
		user.setRole(Roles.ADMIN);
		User save = repo.save(user);
		this.id = save.getUserId();
	}

	@Test
	void testDeleteUser() {
		repo.deleteUser(1231231231L);
		Optional<User> userOptional = repo.findByPhoneNumberAndDeletedAtIsNull(1231231231L);
		assertThat(userOptional).isEmpty();
	}

	@Test
	void testFindByPhoneNumberAndDeletedAtIsNull() {
		Optional<User> result = repo.findByPhoneNumberAndDeletedAtIsNull(1231231231L);
		assertThat(result.get().getPhoneNumber()).isEqualTo(1231231231L);	
	}

	@Test
	void testFindUser() {
		Optional<UserResponseDto> user = repo.findUser(1231231231L);
		assertThat(user).isNotEmpty();
	}

}
