package com.bidding.auction.auctionPortofolio.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidding.auction.auctionPortofolio.dao.AuctionProductRepository;
import com.bidding.auction.auctionPortofolio.dao.BidRepository;
import com.bidding.auction.auctionPortofolio.model.AuctionProduct;
import com.bidding.auction.auctionPortofolio.model.Bid;

@Service
public class AuctionServer {

	@Autowired
	private AuctionProductRepository auctionProductRepository;

	@Autowired
	private BidRepository bidRepository;

	public void registerProduct(String sellerUsername, String productName, int minBid) {
		AuctionProduct product = new AuctionProduct(UUID.randomUUID().toString(), sellerUsername, productName, minBid);
		auctionProductRepository.save(product);
		System.out.println("Product Registered:");
		System.out.println("ProductId: " + product.getProductId());
		System.out.println("ProductName: " + productName);
		System.out.println("MinBid: " + minBid);
	}

	public void placeBid(String buyerUsername, String productId, int bidAmount) {
		validateProductExists(productId);

		Bid bid = new Bid(UUID.randomUUID().toString(), buyerUsername, productId, bidAmount);
		bidRepository.save(bid);

		System.out.println("Bid Placed:");
		System.out.println("BidId: " + bid.getBidId());
		System.out.println("BuyerUsername: " + buyerUsername);
		System.out.println("ProductId: " + productId);
		System.out.println("BidAmount: " + bidAmount);
	}

	public void endAuction(String productId) {
		validateProductExists(productId);

		List<Bid> productBids = bidRepository.findAllByProductIdOrderByBidAmountDesc(productId);

		if (!productBids.isEmpty()) {
			Bid winningBid = productBids.get(0);

			Optional<AuctionProduct> optionalProduct = auctionProductRepository.findById(productId);
			if (optionalProduct.isPresent()) {
				AuctionProduct product = optionalProduct.get();
				product.setWinningBidder(winningBid.getBuyerUsername());
				product.setWinningBidAmount(winningBid.getBidAmount());

				auctionProductRepository.save(product);

				System.out.println("Auction Ended for Product " + productId);
				System.out.println("Winning Bidder: " + product.getWinningBidder());
				System.out.println("Winning Bid Amount: " + product.getWinningBidAmount());
			}
		} else {
			System.out.println("No bids for Product " + productId);
		}
	}

	public List<AuctionProduct> getAllProducts() {
		return auctionProductRepository.findAll();
	}

	public List<Bid> getAllBids() {
		return bidRepository.findAll();
	}

	private void validateProductExists(String productId) {
		if (!auctionProductRepository.existsById(productId)) {
			throw new RuntimeException("Product not found");
		}
	}
}
