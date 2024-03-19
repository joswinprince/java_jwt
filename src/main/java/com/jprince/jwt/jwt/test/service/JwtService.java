package com.jprince.jwt.jwt.test.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.jprince.jwt.jwt.test.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
public class JwtService {
	private final String SECRET_KEY = "d75d1abdb63c41c43f37c07c236b946b5171b43a2fd8f1644e5009425f7444f4";
	public String generateToken(User user)
	{
		String token = Jwts
				.builder()
				.subject(user.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+24*60*60*1000))
				.signWith(getSigninKey())
				.compact();
		return token;
	}
	
	private SecretKey getSigninKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private Claims extractAllClaims(String token)
	{
		return Jwts
				.parser()
				.verifyWith(getSigninKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public <T> T extractClaims(String token,Function<Claims,T> resolver)
	{
		Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}
	
	public String extractUserName(String token)
	{
		return extractClaims(token, Claims::getSubject);
	}
	//Valid if Token is not expired and authenticated user
	public boolean isValid(String token, UserDetails user) {
		String username = extractUserName(token);
		return (username.equals(user.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token, Claims::getExpiration);
	}
}
