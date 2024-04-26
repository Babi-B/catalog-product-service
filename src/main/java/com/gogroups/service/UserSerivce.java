package com.gogroups.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogroups.model.User;
import com.gogroups.repository.UserJpaRepo;

@Service
public class UserSerivce {
	@Autowired
	UserJpaRepo userRepo;

	public User saveUser(User user) {
		return userRepo.save(user);
	}

	public Optional<User> loadUserDetails(String username) {
		return userRepo.findByUsername(username);
	}

	public Optional<User> getUserById(long id) {
		return userRepo.findById(id);
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public void deleteUser(long id) {
		userRepo.deleteById(id);
	}

}
