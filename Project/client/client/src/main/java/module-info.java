module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires Java.WebSocket;

    opens client to javafx.fxml;
    exports client;
}
