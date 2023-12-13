package com.bidding.auction.auctionPortofolio.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidding.auction.auctionPortofolio.JwtTokenUtil;
import com.bidding.auction.auctionPortofolio.model.AuctionProduct;
import com.bidding.auction.auctionPortofolio.model.Bid;
import com.bidding.auction.auctionPortofolio.service.AuctionServer;

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

	@PostMapping("/registerProduct")
	public void registerProduct(HttpServletRequest request, @RequestBody ProductRegistrationRequest productRequest) {
		
		// Extract the token from the Authorization header
//		String token = extractTokenFromRequest(request);
		// Validate the token
//		if (jwtTokenUtil.validateToken(token, productRequest.getSellerUsername())) {
			// Use the token as needed
			auctionServer.registerProduct(jwtTokenUtil.extractUsername(extractTokenFromRequest(request)), productRequest.getProductName(),
					productRequest.getMinBid());
//		} else {
//			throw new RuntimeException("Invalid token");
//		}
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

	@PostMapping("/placeBid")
	public void placeBid(@RequestBody BidRequest request) {
		Bid bid = new Bid();
		bid.setBidId(request.getBidId());
		bid.setBuyerUsername(request.getBuyerUsername());
		bid.setProductId(request.getProductId());
		bid.setBidAmount(request.getBidAmount());

		auctionServer.placeBid(bid.getBuyerUsername(), bid.getProductId(), bid.getBidAmount());
	}

	@GetMapping("/bids")
	public List<Bid> getAllBids() {
		return auctionServer.getAllBids();
	}

	@GetMapping("/endAuction/{productId}")
	public void endAuction(@PathVariable String productId) {
		auctionServer.endAuction(productId);
	}

	static class ProductRegistrationRequest {
		private String productId;
		private String sellerUsername;
		private String productName;
		private int minBid;

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getSellerUsername() {
			return sellerUsername;
		}

		public void setSellerUsername(String sellerUsername) {
			this.sellerUsername = sellerUsername;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public int getMinBid() {
			return minBid;
		}

		public void setMinBid(int minBid) {
			this.minBid = minBid;
		}

	}

	static class BidRequest {
		private String bidId;
		private String buyerUsername;
		private String productId;
		private int bidAmount;

		public String getBidId() {
			return bidId;
		}

		public void setBidId(String bidId) {
			this.bidId = bidId;
		}

		public String getBuyerUsername() {
			return buyerUsername;
		}

		public void setBuyerUsername(String buyerUsername) {
			this.buyerUsername = buyerUsername;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public int getBidAmount() {
			return bidAmount;
		}

		public void setBidAmount(int bidAmount) {
			this.bidAmount = bidAmount;
		}

	}
}
