package org.vitalup.vitalup.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.entities.Auth.Users;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserNameService {

	@Value("${app.jwt.secret}")
	private String secretKey;

	private SecretKey getSigningKey() {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(UserDetails userDetails) {

		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + 1000L * 60 * 60 * 24;

		int passwordVersion = 0;

		if (userDetails instanceof Users) {
			passwordVersion = ((Users) userDetails).getPasswordVersion();
		}

		return Jwts.builder()
			.subject(userDetails.getUsername())
			.claim("passwordVersion", passwordVersion)
			.issuedAt(new Date(nowMillis))
			.expiration(new Date(expMillis))
			.signWith(getSigningKey())
			.compact();
	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = Jwts.parser()
			.verifyWith(getSigningKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
		return resolver.apply(claims);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean validToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username != null &&
			username.equals(userDetails.getUsername()) &&
			!isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		Date expiration = extractClaim(token, Claims::getExpiration);
		return expiration.before(new Date());
	}

	public String generateRegistrationToken(String email) {
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + 1000L * 60 * 5;

		return Jwts.builder()
			.subject(email)
			.claim("purpose", "registration_session")
			.issuedAt(new Date(nowMillis))
			.expiration(new Date(expMillis))
			.signWith(getSigningKey())
			.compact();
	}

	public String generateRefreshToken(Users user) {
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + 1000L * 60 * 60 * 24 * 7;

		return Jwts.builder()
			.subject(user.getUsername())
			.claim("type", "refresh")
			.issuedAt(new Date(nowMillis))
			.expiration(new Date(expMillis))
			.signWith(getSigningKey())
			.compact();
	}

}
