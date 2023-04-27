package com.example.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Service.CustomUserService;
import com.example.Service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private CustomUserService customUserService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authHeader = request.getHeader("Authorization");
		String token= null;
		String username= null;
		if(authHeader!=null && authHeader.startsWith("Bearer")) {
			token = authHeader.substring(7);
			username = jwtService.extractUsername(token);
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails loadUserByUsername = customUserService.loadUserByUsername(username);
			if(jwtService.validateToken(token, loadUserByUsername));
			{
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loadUserByUsername,null,loadUserByUsername.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
