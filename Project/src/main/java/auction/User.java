package auction;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
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

abstract public class User {
    private String login;
    private String password;
    private String email;
    private int balance;


    @FXML
    public ScrollPane scroll_lots;
    @FXML
    public VBox Vbox_lots;
    @FXML
    public TextField add_lot_input;
    @FXML
    public Text add_lot_text;
    @FXML
    public GridPane Menu_grid;
    @FXML
    public GridPane user_info;
    @FXML
    public TextField add_bid_inout;


    private boolean lotIsChecked = false;
    private int lotCheckedID = 0;

    private int addLotStatus = 1;
    private String LOT_NAME = "";
    private String LOT_DESCRIPTION = "";

    // Polymorphism
    public void AddGood() throws SQLException {
        PrintLots();
    }

    public void initialize() throws SQLException {
        PrintLots();
        Platform.runLater(() -> add_lot_input.requestFocus());
    }

    private void PrintLots() throws SQLException {
        Vbox_lots.getChildren().clear();

        ResultSet result = SQL.SELECT_Lots();

        Vbox_lots.setMaxHeight(10);

        while (result.next()) {
            String id = result.getString("id");
            String name = result.getString("name");
            String date = result.getString("date");
            String description = result.getString("description");

            GridPane lot = CreateLotGrid(name, date, description, "seller", id);

            Vbox_lots.getChildren().add(lot);
        }

    }

    private void AddLot_Name(String name) throws ParseException {
        LOT_NAME = name;
        add_lot_input.setText("");
        add_lot_text.setText("Add some beautiful description");

        addLotStatus++;

        Platform.runLater(() -> add_lot_input.requestFocus());
    }

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

    private void AddLot_Description(String input) throws SQLException {
        LOT_DESCRIPTION = input;
        add_lot_input.setText("");
        add_lot_text.setText("Name your lot:");
        addLotStatus = 1;

        SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datenow = new Date();
        String date = DATETIME.format(datenow);
        System.out.println(date);
        SQL.InsertLot(LOT_NAME, LOT_DESCRIPTION, date, Model.getUSER_ID());
        PrintLots();
    }

    private GridPane CreateLotGrid(String name, String date, String description, String seller, String id) {
        Label Lot_name = new Label(name);
        Label Lot_date = new Label(date);
        Label Lot_description = new Label(description);
        Label Lot_seller = new Label(seller);

        Lot_name.setWrapText(true);
        Lot_date.setWrapText(true);
        Lot_description.setWrapText(true);
        Lot_seller.setWrapText(true);

        Lot_date.setStyle("-fx-text-fill: green;  ");
        Lot_description.setStyle("-fx-text-fill: green;  ");
        Lot_seller.setStyle("-fx-text-fill: green;  ");

        GridPane Lot_template = new GridPane();
        Lot_template.setStyle("-fx-background-color: grey;");
        Lot_template.setGridLinesVisible(true);

        Lot_template.add(Lot_name, 0, 0);
        Lot_template.add(Lot_date, 0, 2);
        Lot_template.add(Lot_description, 1, 0);
        Lot_template.add(Lot_seller, 0, 1);

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
            if(!lotIsChecked){
                lotIsChecked = true;
                lotCheckedID = Integer.parseInt(Lot_template.getId());
                Lot_template.setStyle("-fx-background-color: green;");
                System.out.println("Lot checked: " + Lot_template.getId() + ")");
            }else if(lotCheckedID == Integer.parseInt(Lot_template.getId())){
                lotIsChecked = false;
                Lot_template.setStyle("-fx-background-color: grey;");
                System.out.println("Lot UNchecked: " + Lot_template.getId() + ")");
            }
            
        });
        return Lot_template;

    }

}
