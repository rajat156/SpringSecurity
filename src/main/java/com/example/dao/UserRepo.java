package com.example.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{

	public Optional<User> findByUsername(String username);

}
