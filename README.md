# Blindauction-portfolio
Seller &amp; Buyer Biding portfolio
Attached the API document to Bid the auctions in portfolio.
Import the attached REST project for POSTMAN , 

Steps  : GITHub URL :https://github.com/Praneethchakri/BiddingAuction-portfolio
glone the workspace: https://github.com/Praneethchakri/BiddingAuction-portfolio.git  --> Develop Branch


Note: Persistance side of application is on H2 inmemory database with spring JPA to access/process the request.

1.Register the User by providind the required detials 
	--> User registation 
2.Login to the Auction porotofolio with registered user credentials 
	--> security Token generation for respective user to perform actions 
	 like register product ,end the acution , check all the bids, check the list of users services required the jwt token 
3.Register the product with SELLER profile
	--> provide the product details to register the product to portfolio along with jwt token
4. getall the products
	--> list of products application handles
5.getall the users
	--> to identfity the list of Users(Buyers& Selers in portol)
6. Place bid on particular product with minbid amount based on productID
7.getall bids is the service accessable to only the registered to the portal as a SELLER/ Buyer.
8.end the auction of a product ,this service can access only the   registered to the portal as a SELLER.
and close the bit and announce the winner of the product .


Required tools:
Eclipse/any IDE
POSTMan tool 
