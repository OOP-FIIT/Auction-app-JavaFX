package auction;

import java.io.IOException;
import java.sql.SQLException;

import auction.controllers.User;
import auction.shared.Const;
import auction.sql.SQL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFXML App
 */
public class App extends Application {

    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(Const.FXML.LOGIN_SCENE), 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void changeScene(String fxml, User user) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(user);;
        scene.setRoot(fxmlLoader.load());
    }
    /**
     * Start of JavaFXML App, with SQL initialization
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        SQL.initSql();
        launch();
    }


    
}