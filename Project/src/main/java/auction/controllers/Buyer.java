package auction.controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import auction.SQL.SQL;
import auction.exception.BidException;
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

public class Buyer extends User{
    @FXML
    private ScrollPane scroll_lots;
    @FXML
    private VBox Vbox_lots;
    @FXML
    private TextField add_lot_input;
    @FXML
    private TextField add_bid_input;
    @FXML
    private Text add_lot_text;
    @FXML
    private Text add_bid_text;
    @FXML
    private Label userBalance_text;
    @FXML
    private Label user_login_text;
    @FXML
    private GridPane Menu_grid;
    @FXML
    private Button end_lot_auction;

    private final String LOT_BG_COLOR = "TEAL";
    private final String CHECKED_LOT_BG_COLOR = "MEDIUMSPRINGGREEN";

    private boolean lotIsChecked = false;
    private int lotCheckedID = -1;
    private GridPane lotChecked;

    private int addLotStatus = 1;
    private String LOT_NAME = "";
    private String LOT_DESCRIPTION = "";

   
    

}
