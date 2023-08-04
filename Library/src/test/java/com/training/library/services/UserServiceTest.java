package com.training.library.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.training.library.entity.User;
import com.training.library.repositories.EntityGenerator;
import com.training.library.repositories.FloorRepository;
import com.training.library.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

	@Mock
	private UserRepository repo;

	@InjectMocks
	private UserService service;
	@Autowired
	private EntityGenerator entityGenerator;
	

	@Test
	void findByPhoneTest() {
		User user = entityGenerator.getMockUser();
		when(repo.findByPhone(1231231231L)).thenReturn(Optional.of(user));
		User result = service.findByPhone(1231231231L);
		assertThat(result.getPhone()).isEqualTo(1231231231L);
	}
	
	@Test
	void findByPhoneTest2() {
		User user = entityGenerator.getMockUser();
		when(repo.findByPhone(1231231231L)).thenReturn(Optional.empty());
		User result = service.findByPhone(1231231231L);
		assertThat(result).isNull();
	}

	@Test
	void findByIdTest() {
		User user = entityGenerator.getMockUser();
		user.setUserId(0L);
		when(repo.findById(0L)).thenReturn(Optional.of(user));
		User result = service.findById(0L);
		assertThat(result.getUserId()).isEqualTo(0L);
	}

	@Test
	void findByIdTest2() {
		User user = entityGenerator.getMockUser();
		user.setUserId(0L);
		when(repo.findById(0L)).thenReturn(Optional.empty());
		User result = service.findById(0L);
		assertThat(result).isNull();
	}
	@Test
	void saveUserTest() {
		User user = entityGenerator.getMockUser();
		when(repo.save(user)).thenReturn(user);
		User result = service.saveUser(user);
		assertThat(result.getPhone()).isEqualTo(1231231231L);
	}


	@Test
	void newUserTest() {
		User user = entityGenerator.getMockUser();
		when(repo.save(any(User.class))).thenReturn(user);
		User result = service.newUser("1231231231");
		assertThat(result.getPhone()).isEqualTo(1231231231L);
	}

}
