package com.bidding.auction.auctionPortofolio.controller;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidding.auction.auctionPortofolio.JwtTokenUtil;
import com.bidding.auction.auctionPortofolio.dao.AuctionProductRepository;
import com.bidding.auction.auctionPortofolio.dao.UserRepository;
import com.bidding.auction.auctionPortofolio.exceptions.ProductRegistrationException;
import com.bidding.auction.auctionPortofolio.model.AuctionProduct;
import com.bidding.auction.auctionPortofolio.model.AuctionResponse;
import com.bidding.auction.auctionPortofolio.model.Bid;
import com.bidding.auction.auctionPortofolio.model.BidRequest;
import com.bidding.auction.auctionPortofolio.model.ProductRegistrationRequest;
import com.bidding.auction.auctionPortofolio.service.AuctionServer;
import com.bidding.auction.auctionPortofolio.service.CustomUserDetailsService;

import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/auction")
public class AuctionController {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Autowired
	private AuctionServer auctionServer;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	private final UserRepository userRepository;
	private final AuctionProductRepository auctionProductRepository;

	public AuctionController(UserRepository userRepository, AuctionProductRepository auctionProductRepository) {
		this.userRepository = userRepository;
		this.auctionProductRepository = auctionProductRepository;
	}

	@PostMapping(value = "/registerProduct", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerProduct(HttpServletRequest request,
			@Valid @RequestBody ProductRegistrationRequest productRequest) {

		// Extract the token from the Authorization header
		// Validate the token
		// Use the token as needed
		try {
			UserDetails userDetails = new CustomUserDetailsService(userRepository)
					.loadUserByUsername(productRequest.getSellerUsername());
			String tokenUsername = jwtTokenUtil.extractUsername(extractTokenFromRequest(request));
			if (userDetails.getUsername().equals(tokenUsername)) {
				auctionServer.registerProduct(jwtTokenUtil.extractUsername(extractTokenFromRequest(request)),
						productRequest.getProductName(), productRequest.getMinBid());
				return ResponseEntity
						.ok(new AuctionResponse(true, "Product registered successfully!", HttpStatus.OK.value()));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuctionResponse(false,
						"Unauthorized to register Product", HttpStatus.UNAUTHORIZED.value()));
			}
		} catch (ProductRegistrationException e) {
			// If Product registration fails, handle the exception and return an error
			// response
			// Handle unexpected errors
			String errorMessage = e.getMessage() != null ? e.getMessage()
					: "An error occurred during Product registration to the Auction";
			// Return error response
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new AuctionResponse(false, errorMessage, HttpStatus.BAD_REQUEST.value()));
		} catch (Exception e) {
			String errorMessage = e.getMessage() != null ? e.getMessage() : " ";
			// Return error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new AuctionResponse(false, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));

		}
	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7); // Remove "Bearer " prefix
		}
		throw new RuntimeException("Missing or invalid Authorization header");
	}

	private boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GetMapping("/products")
	public List<AuctionProduct> getAllProducts() {
		return auctionServer.getAllProducts();

	}

	@PostMapping(value = "/placeBid", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> placeBid(HttpServletRequest request, @Valid @RequestBody BidRequest bidRequest) {
		UserDetails userDetails = new CustomUserDetailsService(userRepository)
				.loadUserByUsername(bidRequest.getBuyerUsername());
		String tokenUsername = jwtTokenUtil.extractUsername(extractTokenFromRequest(request));
		try {
			if (tokenUsername.equals(userDetails.getUsername())) {
				Bid bid = new Bid();
				bid.setBuyerUsername(jwtTokenUtil.extractUsername(extractTokenFromRequest(request)));
				bid.setProductId(bidRequest.getProductId());
				bid.setBidAmount(bidRequest.getBidAmount());

				auctionServer.placeBid(bid.getBuyerUsername(), bid.getProductId(), bid.getBidAmount());
				return ResponseEntity.ok(new AuctionResponse(true,
						"Auction for Bidding the Product is successful!" + bidRequest.getProductId(),
						HttpStatus.OK.value()));
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuctionResponse(false,
						"You are not the authorized user to place the Bid. Access Forbidden", HttpStatus.FORBIDDEN.value()));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new AuctionResponse(false, e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
		}
	}

	@GetMapping("/bids")
	public List<Bid> getAllBids() {
		return auctionServer.getAllBids();
	}

	@GetMapping("/endAuction/{productId}")
	public ResponseEntity<?> endAuction(HttpServletRequest request, @PathVariable String productId) {
		// Get the authenticated user
		Optional<AuctionProduct> result = auctionProductRepository.findById(productId);
		// result.get().getSellerUsername()
		String tokenUsername = jwtTokenUtil.extractUsername(extractTokenFromRequest(request));
		if (result.get().getSellerUsername().equals(tokenUsername)) {
			AuctionProduct bidWinner = auctionServer.endAuction(productId);
			return ResponseEntity.ok(new AuctionResponse(true, "Bid Winner of Product " + bidWinner.getProductId()
					+ " is  " + bidWinner.getWinningBidder() + " with Bid Amount of " + bidWinner.getWinningBidAmount(),
					HttpStatus.OK.value()));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuctionResponse(false,
					"You are not the Seller to close this Bid. Access Forbidden", HttpStatus.FORBIDDEN.value()));
		}
	}

}
