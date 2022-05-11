package auction.controllers;

import java.io.IOException;

import auction.App;
import auction.shared.Const;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public interface Handler {

    public default void signOutHandle(KeyEvent ke) throws IOException {
        if (ke.getCode().equals(KeyCode.ESCAPE)) {
            App.setRoot(Const.FXML.LOGIN_SCENE);
        }
    }
}
