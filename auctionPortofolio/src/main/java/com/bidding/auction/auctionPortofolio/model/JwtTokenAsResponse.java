package com.bidding.auction.auctionPortofolio.model;

public class JwtTokenAsResponse {

	private String token;

	public JwtTokenAsResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
