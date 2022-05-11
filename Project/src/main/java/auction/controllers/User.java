package auction.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import auction.Model;
import auction.exception.BidException;
import auction.shared.Const;
import auction.sql.SQL;
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

public class User implements Handler{
    @FXML
    protected ScrollPane scroll_lots;
    @FXML
    protected VBox Vbox_lots;
    @FXML
    protected TextField add_lot_input;
    @FXML
    protected TextField add_bid_input;
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
    private GridPane Menu_grid;
    @FXML
    protected GridPane userInfo_GRIDPANE;
    @FXML
    protected GridPane proBannerGrid;
    @FXML
    protected Button end_lot_auction;

    private final String LOT_BG_COLOR = "TEAL";
    private final String CHECKED_LOT_BG_COLOR = "MEDIUMSPRINGGREEN";

    private boolean lotIsChecked = false;
    private int lotCheckedID = -1;
    private GridPane lotChecked;

    private int addLotStatus = 1;
    private String LOT_NAME = "";
    private String LOT_DESCRIPTION = "";


    public void initialize() throws SQLException {
        Vbox_lots.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        scroll_lots.setStyle("-fx-background: DARKSLATEGREY; -fx-border-color: #90EE90;");
        PrintLots();
        Platform.runLater(() -> add_lot_input.requestFocus());
        UpdateUserData();
        add_bid_input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    add_bid_input.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }

    protected void PrintLots() throws SQLException {
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

    private void AddLot_Name(String name) throws ParseException {
        LOT_NAME = name;
        add_lot_input.setText("");
        add_lot_text.setText("Add some beautiful description");

        addLotStatus++;

        Platform.runLater(() -> add_lot_input.requestFocus());
    }

    private void AddLot_Description(String input) throws SQLException {
        LOT_DESCRIPTION = input;
        add_lot_input.setText("");
        add_lot_text.setText("Name your lot:");
        addLotStatus = 1;

        SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datenow = new Date();
        String date = DATETIME.format(datenow);
        SQL.InsertLot(LOT_NAME, LOT_DESCRIPTION, date, Model.getUserId());
        PrintLots();
    }

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

        Lot_template.setOnMouseClicked((event) -> {
            //Check
            if (!lotIsChecked) {
                // Save SQL id, FXML id, status
                lotIsChecked = true;
                lotCheckedID = Integer.parseInt(Lot_template.getId());
                lotChecked = Lot_template;
                Lot_template.setStyle("-fx-background-color: " + CHECKED_LOT_BG_COLOR + ";");
            }//UNcheck 
            else if (lotCheckedID == Integer.parseInt(Lot_template.getId())) {
                lotIsChecked = false;
                lotCheckedID = -1;
                Lot_template.setStyle("-fx-background-color: " + LOT_BG_COLOR + ";");
            }//Check other Lot 
            else {
                Model.setEndAuctionFirstClick(false);
                lotChecked.setStyle("-fx-background-color: " + LOT_BG_COLOR + ";");
                lotCheckedID = Integer.parseInt(Lot_template.getId());
                lotChecked = Lot_template;
                Lot_template.setStyle("-fx-background-color: " + CHECKED_LOT_BG_COLOR + ";");
            }

        });

        Lot_template.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        return Lot_template;

    }

    protected void UpdateUserData() throws SQLException {
        Model.UpdateUser();
        userBalance_text.setText("Balance: " + String.valueOf(Model.getUSER().getBalance()));
        user_login_text.setText(Model.getUSER().getLogin());
    }
    
    
    // Handlers-------------------------------

    public void AddLot_handle(KeyEvent ke) throws ParseException, SQLException, IOException {
        String input = "";
        if (ke.getCode().equals(KeyCode.ENTER)) {
            input = add_lot_input.getText();

            switch (addLotStatus) {
                case 1:
                    AddLot_Name(input);
                    break;
                case 2:
                    AddLot_Description(input);
                    break;
            }

        }
    }

    public void addBid_handle(KeyEvent ke) throws ParseException, SQLException, IOException{
        if (ke.getCode().equals(KeyCode.ENTER) && lotCheckedID != -1) {
            boolean res = false;
            try { 
                res = Model.tryAddBid((add_bid_input.getText()), lotCheckedID);
            }catch(BidException e){
                System.out.println(e);
            }
            
            if(res){
                System.out.println("You have successfully added ew bid(" + add_bid_input.getText() + ") to lot: " + lotCheckedID);
                add_bid_input.setText("");
                add_bid_text.setText("Success!");
                UpdateUserData();
            }
            else{
                add_bid_input.setText("");
                add_bid_text.setText("Your balance and your bid don`t love each other");
            }
    }
    }

    public void endAuction() throws SQLException{
        if(lotCheckedID != -1){
            Model.endAuction(lotCheckedID);
            if(!Model.isEndAuctionFirstClick()){
                PrintLots();
                UpdateUserData();
            }

        }
    }
    
    //Default method inplementation
    public void signOutHandle(KeyEvent ke) throws IOException {
        Handler.super.signOutHandle(ke);
    }

    public void setProBanner(){
        if(this.getClass() == (new Buyer()).getClass())
            currentVersionLable.setText("Buyer");
        if(this.getClass() == (new Auctioner()).getClass())
            currentVersionLable.setText("Auctioner");
        if(this.getClass() == (new Seller()).getClass())
            currentVersionLable.setText("Sellers");
        
        proBannerGrid.setVisible(true);

    }

    public void buyPro() throws NoSuchAlgorithmException, SQLException, IOException{
        Model.setLicenseKey();
    }
}
