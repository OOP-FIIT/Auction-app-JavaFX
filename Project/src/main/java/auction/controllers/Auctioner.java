package auction.controllers;

import java.sql.SQLException;

/**
 * Controller of AUCTIONER mode
 */
public class Auctioner extends User {

    /**
     * Disables feachures of [Adding Lot] and [Adding Bid]
     * Adds "BuyPro" Banner
     */
    @Override
    public void initialize() throws SQLException {
        super.initialize();

        addLotInput.setPromptText("This is PRO feachure");
        addBidInput.setPromptText("You can buy PRO version to use all feachures in one account");
        addLotInput.setDisable(true);
        addBidInput.setDisable(true);
        setProBanner();
    }

}
