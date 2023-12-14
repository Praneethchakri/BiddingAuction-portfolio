package com.bidding.auction.auctionPortofolio.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidding.auction.auctionPortofolio.dao.AuctionProductRepository;
import com.bidding.auction.auctionPortofolio.dao.BidRepository;
import com.bidding.auction.auctionPortofolio.model.AuctionProduct;
import com.bidding.auction.auctionPortofolio.model.Bid;

@Service
public class AuctionServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuctionServer.class);
	@Autowired
	private AuctionProductRepository auctionProductRepository;

	@Autowired
	private BidRepository bidRepository;

	public AuctionServer(AuctionProductRepository auctionProductRepository, BidRepository bidRepository) {
		this.auctionProductRepository = auctionProductRepository;
		this.bidRepository = bidRepository;
	}

	public void registerProduct(String sellerUsername, String productName, int minBid) {
		AuctionProduct product = new AuctionProduct(UUID.randomUUID().toString(), sellerUsername, productName, minBid);
		auctionProductRepository.save(product);
		LOGGER.info("Product Registered:");
		LOGGER.info("ProductId: " + product.getProductId());
		LOGGER.info("ProductName: " + productName);
		LOGGER.info("MinBid: " + minBid);
	}

	public Bid placeBid(String buyerUsername, String productId, int bidAmount) {
		validateProductExists(productId);

		Bid bid = new Bid(UUID.randomUUID().toString(), buyerUsername, productId, bidAmount);
		Bid savedObject = bidRepository.save(bid);

		LOGGER.info("Bid Placed:");
		LOGGER.info("BidId: " + bid.getBidId());
		LOGGER.info("BuyerUsername: " + buyerUsername);
		LOGGER.info("ProductId: " + productId);
		LOGGER.info("BidAmount: " + bidAmount);
		return savedObject;
	}

	public AuctionProduct endAuction(String productId) {
		validateProductExists(productId);

		List<Bid> productBids = bidRepository.findAllByProductIdOrderByBidAmountDesc(productId);
		AuctionProduct product = null;
		if (!productBids.isEmpty()) {
			Bid winningBid = productBids.get(0);

			Optional<AuctionProduct> optionalProduct = auctionProductRepository.findById(productId);
			if (optionalProduct.isPresent()) {
				product = optionalProduct.get();
				product.setWinningBidder(winningBid.getBuyerUsername());
				product.setWinningBidAmount(winningBid.getBidAmount());

				auctionProductRepository.save(product);

				LOGGER.info("Auction Ended for Product " + productId);
				LOGGER.info("Winning Bidder: " + product.getWinningBidder());
				LOGGER.info("Winning Bid Amount: " + product.getWinningBidAmount());
				return product;
			}
		} else {
			System.out.println("No bids for Product " + productId);
		}
		return product;
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
