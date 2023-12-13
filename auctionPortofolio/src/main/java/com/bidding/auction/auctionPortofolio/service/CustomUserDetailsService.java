package com.bidding.auction.auctionPortofolio.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bidding.auction.auctionPortofolio.dao.UserRepository;
import com.bidding.auction.auctionPortofolio.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Ensure that the returned user implements UserDetails
		if (user instanceof UserDetails) {
			return (UserDetails) user;
		} else {
			throw new IllegalStateException("User class must implement UserDetails interface");
		}
	}

}
