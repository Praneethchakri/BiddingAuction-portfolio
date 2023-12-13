package com.bidding.auction.auctionPortofolio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.bidding.auction.auctionPortofolio")
public class AuctionPortofolioApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AuctionPortofolioApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Spring Boot application initialization code
	}
}
