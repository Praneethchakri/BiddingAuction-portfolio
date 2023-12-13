package com.bidding.auction.auctionPortofolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidding.auction.auctionPortofolio.JwtTokenUtil;
import com.bidding.auction.auctionPortofolio.dao.UserRepository;
import com.bidding.auction.auctionPortofolio.exceptions.UserRegistrationException;
import com.bidding.auction.auctionPortofolio.model.JwtTokenAsResponse;
import com.bidding.auction.auctionPortofolio.model.User;
import com.bidding.auction.auctionPortofolio.service.CustomUserDetailsService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping(value="/register",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {

		if (usernameAlreadyExists(request.getUsername())) {
			return ResponseEntity.ok(new UserRegistrationException("Username already exists").getMessage());
		}else {
		try {
			User user = new User();
			user.setUsername(request.getUsername());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			user.setRole(request.getRole());

			userRepository.save(user);
			System.out.println("User Registeration is successful.. " + user.getUsername());
			return ResponseEntity.ok("User registered successfully!");
		} catch (UserRegistrationException e) {
			// If user registration fails, handle the exception and return an error response
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
//		
//		Authentication authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//		
//		// Set authenticated user details in SecurityContextHolder
//		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = new CustomUserDetailsService(userRepository)
				.loadUserByUsername(request.getUsername());
		String token = jwtTokenUtil.generateToken(userDetails.getUsername());

		System.out.println("User details " + userDetails.getUsername() + " Role " + userDetails.getAuthorities());
		return ResponseEntity.ok(new JwtTokenAsResponse(token));
		/*
		 * if (passwordEncoder.matches(request.getPassword(),userDetails.getPassword()))
		 * { return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new
		 * Date()) .signWith(SignatureAlgorithm.HS256, jwtSecret) // Replace with a
		 * securesecret key .compact(); } else { throw new
		 * RuntimeException("Invalid login credentials"); }
		 */
	}

	@GetMapping("/allUsers")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	private boolean usernameAlreadyExists(String username) {
		// Check if the username already exists in the database
		// Example: You would typically query the database here
		boolean userExist = userRepository.existsById(username);
		if (userExist) {
			return true;
		}
		return false;
	}

	static class UserRegistrationRequest {
		private String username;
		private String password;
		private String role;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

	}

	static class UserLoginRequest {
		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}
}
