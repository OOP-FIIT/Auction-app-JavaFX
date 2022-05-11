module auction {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires java.sql;
    requires javax.mail.api;
    requires activation;
    requires java.desktop;
    requires json.simple;

    opens auction to javafx.fxml;
    opens auction.controllers to javafx.fxml;
    exports auction;
    exports auction.controllers;
}
