package com.bidding.auction.auctionPortofolio.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bid {
	@Id
	private String bidId;
	private String buyerUsername;
	private String productId;
	private int bidAmount;

	public Bid() {
		// TODO Auto-generated constructor stub
	}

	public Bid(String bidId, String buyerUsername, String productId, int bidAmount) {
		super();
		this.bidId = bidId;
		this.buyerUsername = buyerUsername;
		this.productId = productId;
		this.bidAmount = bidAmount;
	}

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
