package com.bidding.auction.auctionPortofolio.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
import com.bidding.auction.auctionPortofolio.model.AuctionResponse;
import com.bidding.auction.auctionPortofolio.model.JwtTokenAsResponse;
import com.bidding.auction.auctionPortofolio.model.User;
import com.bidding.auction.auctionPortofolio.model.UserLoginRequest;
import com.bidding.auction.auctionPortofolio.model.UserRegistrationRequest;
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

	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {

		if (usernameAlreadyExists(request.getUsername())) {
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body(new AuctionResponse(false, "Username already exists", HttpStatus.ALREADY_REPORTED.value()));
		} else {
			try {
				User user = new User();
				user.setUsername(request.getUsername());
				user.setPassword(passwordEncoder.encode(request.getPassword()));
				user.setRole(request.getRole());

				userRepository.save(user);
				System.out.println("User Registeration is successful.. " + user.getUsername());
				return ResponseEntity
						.ok(new AuctionResponse(true, "User registered successfully!", HttpStatus.OK.value()));
			} catch (UserRegistrationException e) {
//			// If user registration fails, handle the exception and return an error response
				// Handle unexpected errors
				String errorMessage = e.getMessage() != null ? e.getMessage()
						: "An error occurred during user registration";
				// Return error response
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new AuctionResponse(false, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));
			} catch (Exception e) {
				String errorMessage = e.getMessage() != null ? e.getMessage() : " ";
				// Return error response
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new AuctionResponse(false, errorMessage, HttpStatus.BAD_REQUEST.value()));

			}
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) {
//		
//		Authentication authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//		
//		// Set authenticated user details in SecurityContextHolder
//		SecurityContextHolder.getContext().setAuthentication(authentication);
		try {
			UserDetails userDetails = new CustomUserDetailsService(userRepository)
					.loadUserByUsername(request.getUsername());
			String token = jwtTokenUtil.generateToken(userDetails.getUsername());

			System.out.println("User details " + userDetails.getUsername() + " Role " + userDetails.getAuthorities());
			return ResponseEntity.ok(new JwtTokenAsResponse(token));
		} catch (Exception e) {
			String errorMessage = e.getMessage() != null ? e.getMessage() : " ";
			// Return error response
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new AuctionResponse(false, errorMessage, HttpStatus.BAD_REQUEST.value()));

		}
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

}
