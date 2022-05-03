module client {
    requires transitive Java.WebSocket;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javax.mail.api;
    requires java.desktop;

    opens client to javafx.fxml;
    exports client;
}
