package com.bidding.auction.auctionPortofolio.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bidding.auction.auctionPortofolio.model.AuctionProduct;
@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProduct, String> {

	// Custom queries if needed
}
