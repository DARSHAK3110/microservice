package com.training.library.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.library.entity.User;
import com.training.library.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findByPhone(Long phone) {
		Optional<User> user = this.userRepository.findByPhone(phone);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	public User newUser(String userName) {
		User newUser = new User();
		newUser.setPhone(Long.parseLong(userName));
		return userRepository.save(newUser);
	}
}
