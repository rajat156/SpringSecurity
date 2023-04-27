package com.example.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Entity.User;
import com.example.Service.JWTService;
import com.example.Service.UserService;
import com.example.dto.AuthRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	
	@PostMapping("/saveUser")
	public String saveUser(@RequestBody User user) {
		this.userService.saveUser(user);
		return "User Save Successful";
	}
	
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/getUser")
	public List<User> getAllUser(){
		return this.userService.getAll();
	}
	
	//@PreAuthorize("hasAnyAuthority('USER','ADMIN')
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	@GetMapping("/{username}")
	public User getUserByUsername(@PathVariable("username")String username) {
		return this.userService.getUserByUsername(username);
	}
	
	
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if(authenticate.isAuthenticated())
        return this.jwtService.generateToken(authRequest.getUsername());
	//		return "Successful login";
		else
			throw new UsernameNotFoundException("Invalid User Request");
	}
}
