package com.bidding.auction.auctionPortofolio.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BidRequest {
	
	private String bidId;

	@NotBlank(message = "buyerUsername cannot be blank")
	@Size(min = 3, max = 50, message = "buyerUsername must be between 3 and 50 characters")
	private String buyerUsername;
	
	@NotBlank(message = "productId cannot be blank")
	@Size(min = 3, max = 50, message = "productId must be between 3 and 50 characters")
	private String productId;
	
	@NotNull(message = "bidAmount should be Min  >100 ")
	@Min(value = 0, message = "bidAmount must be between 3 and 50 characters")
	private Integer bidAmount;

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

	public Integer getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(Integer bidAmount) {
		this.bidAmount = bidAmount;
	}

}
