package com.bidding.auction.auctionPortofolio;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
	
	@Value("${jwt.secret}")
	private String jwtSecret;

	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public JwtTokenFilter(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = extractToken(request);
		if (token != null && jwtTokenUtil.validateToken(token, jwtTokenUtil.extractUsername(token))) {
			try {
				// Validate and parse the token using the JWT library
//				Claims claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
//						.parseClaimsJws(token).getBody();

				Claims claims = Jwts.parser().setSigningKey("FDdsdffsDfdxqwedsfdfsdffDSDFSdfdsdfdDFFdsdDdffdsdf")
						.parseClaimsJws(token).getBody();
				// Extract user details from the token
				String username = claims.getSubject();
				List<String> roles = claims.get("roles", List.class); // Adjust as per your token structure

				// Set up authentication context
				if (username != null) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							username, null, null);

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			} catch (Exception e) {
				// Handle token validation or parsing errors
				// You might want to log the error or send a 401 Unauthorized response
				LOGGER.info("Invalid token: " + e.getMessage());
			}
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		// Extract the token from the Authorization header or another location
		// For example, if the token is in the Authorization header as "Bearer <token>"
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7); // Remove "Bearer " prefix
		}
		return null;
	}
}
