package com.bidding.auction.auctionPortofolio;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	// Generate token for user
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}

	// Create token
	private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        LOGGER.info("expirationDate"+expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

	// Extract username from token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Extract expiration date from token
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Extract claim from token
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extract all claims from token
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	// Check if token is expired
	private Boolean isTokenExpired(String token) {
		Date expiration = extractExpiration(token);
		return expiration.before(new Date());
	}

	// Validate token
	public Boolean validateToken(String token, String username) {
		return (username.equals(extractUsername(token)) && !isTokenExpired(token));
	}
}
