package auction.controllers;

import java.sql.SQLException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import auction.Model;
import auction.SQL.SQL;
import auction.exception.BidException;
import auction.shared.Const;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.converter.DateTimeStringConverter;

public class Seller extends User{
        //Polymorphism
        public void handle_button(){
            //Here will be code for handling Seller button click
            //Seller could add new goods and this starts new auction
            //And he could start auction with this button
        }

        @Override
        public void initialize() throws SQLException {
            add_bid_input.setPromptText("You can buy PRO version to use all feachures in one account");
            add_bid_input.setDisable(true);
            end_lot_auction.setDisable(true);
            Vbox_lots.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            scroll_lots.setStyle("-fx-background: DARKSLATEGREY; -fx-border-color: #90EE90;");
            PrintLots();
            Platform.runLater(() -> add_lot_input.requestFocus());
            UpdateUserData();
    
        }

}
