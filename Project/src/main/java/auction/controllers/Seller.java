package auction.controllers;

import java.sql.SQLException;

/**
 * Controller of SELLER mode
 */
public class Seller extends User {

    /**
     * Disables feachures of [Adding Bid] and [Ending Auction]
     * Adds "BuyPro" Banner
     */
    public void initialize() throws SQLException {
        super.initialize();

        addBidInput.setPromptText("You can buy PRO version to use all feachures in one account");
        addBidInput.setDisable(true);
        endAuctionButton.setDisable(true);
        proBannerGrid.setVisible(true);
        setProBanner();
    }

}
