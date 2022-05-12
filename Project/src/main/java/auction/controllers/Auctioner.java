package auction.controllers;

import java.sql.SQLException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Auctioner extends User {

    // Polymorphism
    public void handle_button() {
        // Here will be code for handling auctioner button click
        // Auctioner could finish auction and calculate winner
        // And he could stop auction with this button
    }

    @Override
    public void initialize() throws SQLException {
        add_lot_input.setDisable(true);
        add_lot_text.setText("Try PRO version to use this)");
        add_bid_input.setPromptText("You can buy PRO version to use all feachures in one account");
        add_bid_input.setDisable(true);
        Vbox_lots.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        scroll_lots.setStyle("-fx-background: DARKSLATEGREY; -fx-border-color: #90EE90;");
        
        PrintLots();
        Platform.runLater(add_lot_input::requestFocus);
        UpdateUserData();
        setProBanner();

    }


}
