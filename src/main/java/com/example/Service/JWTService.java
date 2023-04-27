package com.example.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {

	private String tokenForUser;
	
	public void setTokenForUser(String tokenForUser) {
		this.tokenForUser = tokenForUser;
	}

	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	public String getUserNameByToken() {
		return this.extractUsername(this.tokenForUser);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Claims extractAllClaims(String token) {
		return 	Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		}
	
	  private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }
	  
	  //Check token is valid or not
	  public Boolean validateToken(String token, UserDetails userDetails)  {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
	  
	  
	  //Generate Token with help of username
	  public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		
		return createToken(claims,username);
	  }


	 //token create here with the help os Jwts class
	private String createToken(Map<String, Object> claims, String username) {
		// TODO Auto-generated method stub
		return	Jwts.builder()
			.setClaims(claims)
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+3600000))
			.signWith(getSignKey(),SignatureAlgorithm.HS256)
			.compact();
	}

	private Key getSignKey() {
		// TODO Auto-generated method stub
			byte[] keyBytes = Decoders.BASE64.decode("6D5A7134743777217A24432646294A404E635266556A586E3272357538782F413F442A472D4B6150645367566B59703373367639792442264529482B4D625165");
			return	Keys.hmacShaKeyFor(keyBytes);
		
	}
}
