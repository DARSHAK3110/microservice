package com.training.authentication.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY = "368E615852A5885E69A591AD26B3711111111111111111111111111111111111111";
	private static final String REFRESH_SECRET_KEY = "368E615852A5885E69A591111B3711111111111111111111111111111111111111";

	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token,"access");
		return claimResolver.apply(claims);
	}

	public Claims extractAllClaims(String token, String tokenType) {
		if (tokenType.equals("access")) {
			return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(getKeys(SECRET_KEY))).build()
					.parseClaimsJws(token).getBody();
		} else {
			return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(getKeys(REFRESH_SECRET_KEY))).build()
					.parseClaimsJws(token).getBody();
		}
	}

	public String generateToken(UserDetails customUserDetails) {
		return generateToken(customUserDetails, new HashMap<>());
	}

	public String generateRefreshToken(Long phoneNumer) {
		return Jwts.builder().setSubject(phoneNumer.toString()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 10 * 1000))
				.signWith(Keys.hmacShaKeyFor(getKeys(REFRESH_SECRET_KEY)), SignatureAlgorithm.HS256).compact();
	}

	public String generateToken(UserDetails customUserDetails, Map<String, Object> claims) {

		return Jwts.builder().setClaims(claims).setSubject(customUserDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60* 10 * 1000))
				.signWith(Keys.hmacShaKeyFor(getKeys(SECRET_KEY)), SignatureAlgorithm.HS256).compact();
	}

	private byte[] getKeys(String key) {
		return Decoders.BASE64.decode(key).clone();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpirationDate(token).before(new Date(System.currentTimeMillis()));
	}

	private Date extractExpirationDate(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
