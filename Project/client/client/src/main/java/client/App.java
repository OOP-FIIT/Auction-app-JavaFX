package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


/**
 * JavaFX App
 */
public class App extends Application {
    public static WebSocketClient io;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws URISyntaxException, SQLException {
      SQL.InitSql();

        io = new WebSocketClient(new URI("ws://localhost:8887")) {

          
            @Override
            public void onOpen(ServerHandshake handshakedata) {
              send("Hello, it is me. Mario :)");
              System.out.println("opened connection");
              // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
            }
          
            @Override
            public void onMessage(String message) {
              System.out.println("received: " + message);
            }
          
            @Override
            public void onClose(int code, String reason, boolean remote) {
              // The close codes are documented in class org.java_websocket.framing.CloseFrame
              System.out.println(
                  "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                      + reason);
            }
          
            @Override
            public void onError(Exception ex) {
              ex.printStackTrace();
              // if the error is fatal then onClose will be called additionally
            }
          
          };
          io.connect();
                  launch();

    }

}