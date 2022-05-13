package auction.controllers;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import auction.App;
import auction.Auction;
import auction.dataBase.SQL;
import auction.exception.BidException;
import auction.shared.Const;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller of PRO mode
 */
public class User implements Handler {
    @FXML
    protected ScrollPane scroll_lots;
    @FXML
    protected VBox Vbox_lots;
    @FXML
    protected TextField addLotInput;
    @FXML
    protected TextField addBidInput;
    @FXML
    protected Text add_lot_text;
    @FXML
    private Text add_bid_text;
    @FXML
    private Label userBalance_text;
    @FXML
    private Label user_login_text;
    @FXML
    private Label currentVersionLable;
    @FXML
    private Label haveProLable;
    @FXML
    private GridPane Menu_grid;
    @FXML
    protected GridPane userInfo_GRIDPANE;
    @FXML
    protected GridPane proBannerGrid;
    @FXML
    protected Button endAuctionButton;

    private static final String LOT_BG_COLOR = "TEAL";
    private static final String CHECKED_LOT_BG_COLOR = "MEDIUMSPRINGGREEN";

    private boolean lotIsChecked = false;
    private int lotCheckedID = -1;
    private GridPane lotChecked;

    private int addLotStatus = 1;
    private String LOT_NAME = "";
    private String LOT_DESCRIPTION = "";

    public void initialize() throws SQLException {
        Vbox_lots.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        scroll_lots.setStyle("-fx-background: DARKSLATEGREY; -fx-border-color: #90EE90;");
        printLots();
        Platform.runLater(addLotInput::requestFocus);
        updateUserData();
        addBidInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    addBidInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }

        
    /**
     * Shows custom banner depends on user`s mode
     */
    protected void setProBanner() {
        // RTTI implmentation
        if (this.getClass() == (new Buyer()).getClass())
            currentVersionLable.setText("Buyer");
        if (this.getClass() == (new Auctioner()).getClass())
            currentVersionLable.setText("Auctioner");
        if (this.getClass() == (new Seller()).getClass())
            currentVersionLable.setText("Sellers");

        proBannerGrid.setVisible(true);

    }
    
    /**
     * Prints every lot that is stored in DataBase 
     * @throws SQLException
     */
    protected void printLots() throws SQLException {
        Vbox_lots.getChildren().clear();

        ResultSet lot = SQL.SELECT_Lots();

        Vbox_lots.setMaxHeight(50);

        while (lot.next()) {
            String id = lot.getString(Const.SQL.LOTS_ID);
            String name = lot.getString(Const.SQL.LOTS_NAME);
            String date = lot.getString(Const.SQL.LOTS_DATE);
            String description = lot.getString(Const.SQL.LOTS_DESCRIPTION);
            ResultSet user = SQL.SELECT_UserData(lot.getInt(Const.SQL.LOTS_SELLER_ID));
            user.next();
            GridPane lotGrid = getLotGrid(name, date, description, user.getString(Const.SQL.USERDATA_LOGIN), id);

            Vbox_lots.getChildren().add(lotGrid);
        }

    }

    /**
     * Saves name of Lot
     * and swithes to addLotDescription mode
     * @param name
     * @throws ParseException
     */
    private void addLotName(String name) throws ParseException {
        LOT_NAME = name;
        addLotInput.setText("");
        add_lot_text.setText("Add some beautiful description");

        addLotStatus++;

        Platform.runLater(addLotInput::requestFocus);
    }

    /**
     * Saves description
     * then push it in DataBase
     * then updates Lot view 
     * @param input
     * @throws SQLException
     */
    private void addLotDescription(String input) throws SQLException {
        LOT_DESCRIPTION = input;
        addLotInput.setText("");
        add_lot_text.setText("Name your lot:");
        addLotStatus = 1;

        SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datenow = new Date();
        String date = DATETIME.format(datenow);
        SQL.InsertLot(LOT_NAME, LOT_DESCRIPTION, date, Auction.getUserId());
        printLots();
    }

    /**
     * Returns FXML GridPane with all info in arguments
     * @param name
     * @param date
     * @param description
     * @param seller
     * @param id
     * @return
     */
    private GridPane getLotGrid(String name, String date, String description, String seller, String id) {
        Label Lot_name = new Label(name);
        Label Lot_date = new Label(date);
        Label Lot_description = new Label(description);
        Label Lot_seller = new Label("by: " + seller);

        Lot_name.setWrapText(true);
        Lot_date.setWrapText(true);
        Lot_description.setWrapText(true);
        Lot_seller.setWrapText(true);

        Lot_date.setStyle("-fx-text-fill: black; -fx-font-size: 14;");
        Lot_name.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
        Lot_description.setStyle("-fx-text-fill: green;  ");
        Lot_seller.setStyle("-fx-text-fill: red; -fx-font-size: 24;");

        GridPane Lot_template = new GridPane();
        Lot_template.setStyle("-fx-background-color: " + LOT_BG_COLOR + ";");
        Lot_template.setGridLinesVisible(true);

        Lot_template.add(Lot_name, 0, 0);
        Lot_template.add(Lot_date, 0, 2);
        Lot_template.add(Lot_description, 1, 0);
        Lot_template.add(Lot_seller, 0, 1);
        Lot_template.maxHeight(30);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(15);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(75);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(10);
        Lot_template.getRowConstraints().addAll(row1, row2, row3);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(15);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(85);
        Lot_template.getColumnConstraints().addAll(col1, col2);

        Lot_template.setId(id);
        Lot_template.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        Lot_template.setOnMouseClicked(event -> {
            // Check
            if (!lotIsChecked) {
                // Save SQL id, FXML id, status
                lotIsChecked = true;
                lotCheckedID = Integer.parseInt(Lot_template.getId());
                lotChecked = Lot_template;
                Lot_template.setStyle("-fx-background-color: " + CHECKED_LOT_BG_COLOR + ";");
            } // UNcheck
            else if (lotCheckedID == Integer.parseInt(Lot_template.getId())) {
                lotIsChecked = false;
                lotCheckedID = -1;
                Lot_template.setStyle("-fx-background-color: " + LOT_BG_COLOR + ";");
            } // Check other Lot
            else {
                Auction.setEndAuctionFirstClick(false);
                lotChecked.setStyle("-fx-background-color: " + LOT_BG_COLOR + ";");
                lotCheckedID = Integer.parseInt(Lot_template.getId());
                lotChecked = Lot_template;
                Lot_template.setStyle("-fx-background-color: " + CHECKED_LOT_BG_COLOR + ";");
            }

        });

        Lot_template.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        return Lot_template;

    }

    /**
     * Updates UserData with new frim SQL
     * @throws SQLException
     */
    protected void updateUserData() throws SQLException {
        Auction.updateUser();
        userBalance_text.setText("Balance: " + String.valueOf(Auction.getUSER().getBalance()));
        user_login_text.setText(Auction.getUSER().getLogin());
    }

    // Handlers-------------------------------

    public void addLotInputHandle(KeyEvent ke) throws ParseException, SQLException, IOException {
        String input = "";
        if (ke.getCode().equals(KeyCode.ENTER)) {
            input = addLotInput.getText();

            switch (addLotStatus) {
                case 1:
                    addLotName(input);
                    break;
                case 2:
                    addLotDescription(input);
                    break;
            }

        }
    }

    public void addBidInputHandle(KeyEvent ke) throws ParseException, SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER) && lotCheckedID != -1) {
            boolean res = false;
            try {
                res = Auction.tryAddBid((addBidInput.getText()), lotCheckedID);
            } catch (BidException e) {
                System.out.println(e);
            }

            if (res) {
                System.out.println(
                        "You have successfully added ew bid(" + addBidInput.getText() + ") to lot: " + lotCheckedID);
                addBidInput.setText("");
                add_bid_text.setText("Success!");
                updateUserData();
            } else {
                addBidInput.setText("");
                add_bid_text.setText("Your balance and your bid don`t love each other");
            }
        }
    }

    public void endAuctionButtonHandle() throws SQLException {
        if (lotCheckedID != -1) {
            Auction.endAuction(lotCheckedID);
            if (!Auction.isEndAuctionFirstClick()) {
                printLots();
                updateUserData();
            }

        }
    }

    public void signOutEscHandle(KeyEvent ke) throws IOException {
            // Default method inplementation
        Handler.super.signOutHandle(ke);
    }

    public void buyProButtonHandle() throws NoSuchAlgorithmException, SQLException, IOException {
        Auction.setLicenseKey();
    }

    public void iHaveProButtonHandle() throws SQLException, IOException {
        FileChooser fileChooser = new FileChooser();
        File licenseFile = fileChooser.showOpenDialog(new Stage());
        if (Auction.verifyLicense(licenseFile)) {
            App.changeScene(Const.FXML.AUCTION_SCENE, new User());
        } else {
            haveProLable.setText("Sorry, but we cannot verify this license, try another one, that maches your account");
        }
    }

}
