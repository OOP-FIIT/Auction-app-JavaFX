package auction;

import auction.controllers.User;
import auction.dataBase.SQL;
import auction.shared.Const;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 应用程序
 * Main JavaFXML App
 *
 * @author MS
 * @date 2022 /05/15
 */
public class App extends Application {

    /**
     * The constant scene.
     */
    public static Scene scene;

    
    /** 
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(Const.FXML.LOGIN_SCENE), 600, 400);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Sets root.
     *
     * @param fxml the fxml
     * @throws IOException the io exception
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    
    /** 
     * @param fxml
     * @return Parent
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    /**
     * Change scene.
     *
     * @param fxml the fxml
     * @param user the user
     * @throws IOException the io exception
     */
    public static void changeScene(String fxml, User user) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(user);;
        scene.setRoot(fxmlLoader.load());
    }

    /**
     * Start of JavaFXML App, with SQL initialization
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String[] args) throws SQLException {
        SQL.initSql();
        launch();
    }


    
}