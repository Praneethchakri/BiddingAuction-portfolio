package com.bidding.auction.auctionPortofolio.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuctionResponse {
	@JsonProperty("success")
	private boolean success;
	@JsonProperty("message")
	private String message;
	@JsonProperty("statusCode")
	private int statusCode;

	// Constructors, getters, and setters

	// Constructor for success response
	public AuctionResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	// Constructor for failure response with error code
	public AuctionResponse(boolean success, String message, int statusCode) {
		this.success = success;
		this.message = message;
		this.statusCode = statusCode;
	}

}
