package com.bidding.auction.auctionPortofolio.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bidding.auction.auctionPortofolio.model.Bid;
@Repository
public interface BidRepository extends JpaRepository<Bid, String> {
	List<Bid> findAllByProductIdOrderByBidAmountDesc(String productId);
}
