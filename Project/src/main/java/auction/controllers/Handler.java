package auction.controllers;

import java.io.IOException;

import auction.App;
import auction.shared.Const;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The interface Handler.
 */
public interface Handler {

    /**
     * Sign out handle.
     *
     * @param ke the ke
     * @throws IOException the io exception
     */
    public default void signOutHandle(KeyEvent ke) throws IOException {
        if (ke.getCode().equals(KeyCode.ESCAPE)) {
            App.setRoot(Const.FXML.LOGIN_SCENE);
        }
    }
}
