package auction;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.DateTimeStringConverter;

public class Buyer extends User {
    @FXML
    private ScrollPane scroll_lots;
    @FXML
    private VBox Vbox_lots;
    @FXML
    private TextField add_lot_input;
    @FXML
    private Text add_lot_text;

    private int addLotStatus = 1;
    private String LOT_NAME = "";
    private String LOT_DESCRIPTION = "";

    // Polymorphism
    public void AddGood() throws SQLException {
        SQL.InsertLot("name", "1000-10-10 10:10:10", "description");
        PrintLots();
    }

    public void initialize() throws SQLException {
        PrintLots();
        Platform.runLater(() -> add_lot_input.requestFocus());
    }

    private void PrintLots() throws SQLException {
        Vbox_lots.getChildren().clear();

        ResultSet result = SQL.SELECT_Lots();

        while (result.next()) {
            String id = result.getString("id");
            String name = result.getString("name");
            String date = result.getString("date");

            Text lot = new Text("ID: " + id + "\t Name: " + name + "\t Date: " + date);
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
        SQL.InsertLot(LOT_NAME, LOT_DESCRIPTION, date);
        PrintLots();
    }

}
