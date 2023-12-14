package com.bidding.auction.auctionPortofolio.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductRegistrationRequest {
	private String productId;
	@NotBlank(message = "sellerUsername cannot be blank")
	@Size(min = 3, max = 50, message = "sellerUsername must be between 3 and 50 characters")
	private String sellerUsername;
	@NotBlank(message = "productName cannot be blank")
	@Size(min = 3, max = 50, message = "productName must be between 3 and 50 characters")
	private String productName;
	@NotNull(message = "bidAmount should be Min  >100 ")
	@Min(value = 0, message = "bidAmount must be between 3 and 50 characters")
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
