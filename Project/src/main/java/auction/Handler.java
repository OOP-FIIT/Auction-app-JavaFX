package auction;

import java.io.IOException;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public interface Handler {
    
    void sign_out_handle(KeyEvent ke) throws IOException;
}
