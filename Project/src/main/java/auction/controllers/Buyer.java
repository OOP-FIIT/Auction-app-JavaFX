package auction.controllers;

import java.sql.SQLException;

import javafx.application.Platform;

/**
 * Controller of BUYER mode
 */
public class Buyer extends User {

    /**
     * Disables feachures of [Adding Lot] and [Ending Auction]
     * Adds "BuyPro" Banner
     */
    @Override
    public void initialize() throws SQLException {
        super.initialize();

        addLotInput.setPromptText("Try PRO version to use this");
        addLotInput.setDisable(true);
        endAuctionButton.setDisable(true);
        Platform.runLater(addBidInput::requestFocus);
        setProBanner();
    }

}
