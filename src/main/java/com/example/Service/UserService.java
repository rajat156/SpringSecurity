package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Entity.User;
import com.example.dao.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	public User saveUser(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		return this.userRepo.saveAndFlush(user);
	}
	
	public List<User> getAll(){
		return this.userRepo.findAll();
	}

	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		 Optional<User> findByUsername = this.userRepo.findByUsername(username);
		return findByUsername.get();
	}
	
}
