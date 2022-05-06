package auction;

import java.io.IOException;
import java.sql.SQLException;

import auction.SQL.SQL;
import auction.controllers.User;
import auction.shared.Const;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

 
    // constructor to put ip address and port

    private static Scene scene;

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

    public static void main(String[] args) throws SQLException {
        SQL.InitSql();
        launch();
    }


    
}