package com.bidding.auction.auctionPortofolio.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AuctionProduct {
    @Id
    private String productId;
    private String sellerUsername;
    private String productName;
    private int minBid;
    private String winningBidder; 
    private int winningBidAmount; 
    
    
	public AuctionProduct() {
		// TODO Auto-generated constructor stub
	}

	public AuctionProduct(String productId, String sellerUsername, String productName, int minBid, String winningBidder,
			int winningBidAmount) {
		super();
		this.productId = productId;
		this.sellerUsername = sellerUsername;
		this.productName = productName;
		this.minBid = minBid;
		this.winningBidder = winningBidder;
		this.winningBidAmount = winningBidAmount;
	}
	

	public AuctionProduct(String productId, String sellerUsername, String productName, int minBid) {
		super();
		this.productId = productId;
		this.sellerUsername = sellerUsername;
		this.productName = productName;
		this.minBid = minBid;
	}

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

	public String getWinningBidder() {
		return winningBidder;
	}

	public void setWinningBidder(String winningBidder) {
		this.winningBidder = winningBidder;
	}

	public int getWinningBidAmount() {
		return winningBidAmount;
	}

	public void setWinningBidAmount(int winningBidAmount) {
		this.winningBidAmount = winningBidAmount;
	}
	
    
}
