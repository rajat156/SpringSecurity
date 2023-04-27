package com.example.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.Configuration.UserInfoConfiguration;
import com.example.Entity.User;
import com.example.dao.UserRepo;

@Component
public class CustomUserService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> findByUsername = this.userRepo.findByUsername(username);
	return findByUsername.map(UserInfoConfiguration::new)
						.orElseThrow(()-> new UsernameNotFoundException("Login Details are not true"));
		
	}

}
