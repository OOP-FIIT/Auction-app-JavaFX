module auction {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javax.mail.api;
    requires java.desktop;

    opens auction to javafx.fxml;
    exports auction;
}
