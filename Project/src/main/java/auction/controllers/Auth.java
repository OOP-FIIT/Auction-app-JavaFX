package auction.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javax.mail.*;
import javax.mail.internet.*;

import auction.App;
import auction.Auction;
import auction.shared.Const;
import auction.sql.SQL;

/**
 * Controller of Sign in/on window
 */
public class Auth {
    @FXML
    private TextField loginInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField emailInput;
    @FXML
    private Text loginText;
    @FXML
    private Text passwordText;
    @FXML
    private Text emailText;
    @FXML
    private GridPane userModeGrid;
    private Random rand;
    private int activationCode;
    private boolean signInMode = true;
    private String savedPassword = "";
    private boolean repeatPassword = false;
    private boolean emailMode = true;

    private String userPasswordFinal;
    private String userLoginFinal;
    private String userEmailFinal;
    private int randomBalance = 100;

    public void initialize() {
        Platform.runLater(loginInput::requestFocus);
    }

    /**
     * Starts Sign In if login is exists
     * Starts Sign On if login is new
     * @param login
     * @throws IOException
     * @throws SQLException
     */
    private void checkLogin(String login) throws IOException, SQLException {
        if (SQL.loginExists(login)) {
            signInMode = true;
            signIn();
        } else {
            signInMode = false;
            signUp(login);
        }
    }

    /**
     * Checks password for Sign In and Sign On 
     * Calls nex function if password is correct
     * @param login
     * @param password
     * @throws SQLException
     * @throws IOException
     */
    private void checkPassword(String login, String password) throws SQLException, IOException {
        if (signInMode) {
            int userID = SQL.IsPaaswordCorrect(login, password);
            Auction.setUserId(userID);
            Auction.updateUser();
            String mode = Auction.getUSER().getMode();
            if (userID != 0)
                switchToMenu(mode, userID);
            else {
                passwordInput.setText("");
                passwordText.setText("Please, try again");
            }
        } else {
            if (!repeatPassword) {
                savedPassword = password;
                repeatPassword = true;
                passwordInput.setText("");
                passwordText.setText("Please, repeat password");
                Platform.runLater(passwordInput::requestFocus);
            } else {
                if (savedPassword.equals(password)) {
                    passwordInput.setText("");
                    passwordText.setText("Done!");
                    emailInput.setVisible(true);
                    emailText
                            .setStyle("visibility: visible; -fx-background-color: grey; alligment: center; ");
                    userPasswordFinal = password;
                    passwordInput.setDisable(true);
                    Platform.runLater(emailInput::requestFocus);
                } else {
                    savedPassword = "";
                    repeatPassword = false;
                    passwordInput.setText("");
                    passwordText.setText("Create password for account");
                    Platform.runLater(passwordInput::requestFocus);
                }
            }
        }
    }

    /**
     * Starts sign In process
     */
    private void signIn() {
        passwordText.setVisible(true);
        passwordInput.setVisible(true);
        Platform.runLater(passwordInput::requestFocus);
    }

    /**
     * Starts Sign Up process
     * @param login
     */
    private void signUp(String login) {
        passwordText.setVisible(true);
        passwordInput.setVisible(true);
        passwordText.setText("Create password for account");
        loginText.setText("Great name");
        userLoginFinal = login;
        loginInput.setDisable(true);
        Platform.runLater(passwordInput::requestFocus);
    }

    /**
     * Checks email, then sends activation mail
     * When email sended, check activation code
     * @param email
     */
    private void checkEmail(String email) {
        if (emailMode) {
            Auction.sendActivationMail(email);
            emailText.setText("Super, now just put your verification code here");
            emailInput.clear();
            emailMode = false;
            userEmailFinal = email;
            rand = new Random();
            randomBalance = rand.nextInt(100000);
        } else {
            if (Integer.parseInt(email) == activationCode) {
                emailText.setText("Excellent, last thing to do - choose yours account mode");
                userModeGrid.setVisible(true);
                emailInput.setDisable(true);
            }
        }
    }


    /**
     * Swithes window to new
     * @param mode sets access permissions to Auction [PRO, SELLER, BUYER, AUCTIONER] 
     * @param userID user`s ID in DataBase
     * @throws IOException
     * @throws SQLException
     */
    private void switchToMenu(String mode, int userID) throws IOException, SQLException {
        Auction.setUserId(userID);
        if (mode.equals(Const.SQL.USER_MODE_PRO)) {
            if (Auction.verifyLicense(null))
                App.changeScene(Const.FXML.AUCTION_SCENE, new User());
            else
                App.changeScene(Const.FXML.AUCTION_SCENE, new Seller());

        } else if (mode.equals(Const.SQL.USER_MODE_AUCTIONER)) {
            App.changeScene(Const.FXML.AUCTION_SCENE, new Auctioner());
        } else if (mode.equals(Const.SQL.USER_MODE_BUYER)) {
            App.changeScene(Const.FXML.AUCTION_SCENE, new Buyer());
        } else if (mode.equals(Const.SQL.USER_MODE_SELLER)) {
            App.changeScene(Const.FXML.AUCTION_SCENE, new Seller());
        }

    }


    // HANDLERS ---------------------------------------------------------------------------------------
    /**
     * Creates new user in DataBase and swithes to BUYER mode
     * @throws SQLException
     * @throws IOException
     */
    public void buyerModeHandle() throws SQLException, IOException {
        int userID = SQL.InsertUser(userLoginFinal, userPasswordFinal, userEmailFinal, Const.SQL.USER_MODE_BUYER, randomBalance);
        switchToMenu(Const.SQL.USER_MODE_PRO, userID);
    }

    /**
     * Creates new user in DataBase and swithes to SELLER mode
     * @throws SQLException
     * @throws IOException
     */
    public void sellerModeHandle() throws SQLException, IOException {
        int userID = SQL.InsertUser(userLoginFinal, userPasswordFinal, userEmailFinal, Const.SQL.USER_MODE_SELLER,
                randomBalance);
        switchToMenu(Const.SQL.USER_MODE_SELLER, userID);
    }

    /**
     * Creates new user in DataBase and swithes to AUCTIONER mode
     * @throws SQLException
     * @throws IOException
     */
    public void auctionerModeHandle() throws SQLException, IOException {
        int userID = SQL.InsertUser(userLoginFinal, userPasswordFinal, userEmailFinal, Const.SQL.USER_MODE_AUCTIONER,
                randomBalance);
        switchToMenu(Const.SQL.USER_MODE_PRO, userID);
    }

    /**
     * Starts checkLogin() if ENTER pressed in login input
     * @param ke
     * @throws SQLException
     * @throws IOException
     */
    public void loginInputHandle(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = loginInput.getText();
            checkLogin(login);
        }
    }

    /**
     * Starts checkPassword() if ENTER pressed in password input
     * @param ke
     * @throws SQLException
     * @throws IOException
     */
    public void passwordInputHandle(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = loginInput.getText();
            String password = passwordInput.getText();
            checkPassword(login, password);
        }
    }

    /**
     * Starts checkEmail() if ENTER pressed on email input
     * @param ke
     */
    public void emailInputHande(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String email = emailInput.getText();
            checkEmail(email);
        }
    }

}