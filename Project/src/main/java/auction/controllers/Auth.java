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
import auction.Model;
import auction.shared.Const;
import auction.sql.SQL;

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

    private void checkLogin(String login) throws IOException, SQLException {
        if (SQL.IsLoginExists(login)) {
            signInMode = true;
            signIn();
        } else {
            signInMode = false;
            signUp(login);
        }
    }

    private void checkPassword(String login, String password) throws SQLException, IOException {
        if (signInMode) {
            int userID = SQL.IsPaaswordCorrect(login, password);
            Model.setUserId(userID);
            Model.updateUser();
            String mode = Model.getUSER().getMode();
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

    private void signIn() {
        passwordText.setVisible(true);
        passwordInput.setVisible(true);
        Platform.runLater(passwordInput::requestFocus);
    }

    private void signUp(String login) {
        passwordText.setVisible(true);
        passwordInput.setVisible(true);
        passwordText.setText("Create password for account");
        loginText.setText("Great name");
        userLoginFinal = login;
        loginInput.setDisable(true);
        Platform.runLater(passwordInput::requestFocus);
    }

    private void checkEmail(String email) {
        if (emailMode) {
            sendActivationMail(email);
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

    private void sendActivationMail(String reciever) {

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", Const.MAIL.HOST);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Const.MAIL.LOGIN, Const.MAIL.PASSWORD);
            }
        });

        session.setDebug(false);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(Const.MAIL.LOGIN));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(reciever));
            rand = new Random();
            // Set Subject: header field
            activationCode = rand.nextInt(100000);
            message.setSubject("Code: " + activationCode);

            message.setText(Const.MAIL.getCodeMessage(activationCode));

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    private void switchToMenu(String mode, int userID) throws IOException, SQLException {
        Model.setUserId(userID);
        if (mode.equals(Const.SQL.USER_MODE_PRO)) {
            if (Model.verifyLicense(null))
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

    public void buyerModeHandle() throws SQLException, IOException {
        int userID = SQL.InsertUser(userLoginFinal, userPasswordFinal, userEmailFinal, Const.SQL.USER_MODE_BUYER, randomBalance);
        switchToMenu(Const.SQL.USER_MODE_PRO, userID);
    }

    public void sellerModeHandle() throws SQLException, IOException {
        int userID = SQL.InsertUser(userLoginFinal, userPasswordFinal, userEmailFinal, Const.SQL.USER_MODE_SELLER,
                randomBalance);
        switchToMenu(Const.SQL.USER_MODE_SELLER, userID);
    }

    public void auctionerModeHandle() throws SQLException, IOException {
        int userID = SQL.InsertUser(userLoginFinal, userPasswordFinal, userEmailFinal, Const.SQL.USER_MODE_AUCTIONER,
                randomBalance);
        switchToMenu(Const.SQL.USER_MODE_PRO, userID);
    }

    public void loginInputHandle(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = loginInput.getText();
            checkLogin(login);
        }
    }

    public void passwordInputHandle(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = loginInput.getText();
            String password = passwordInput.getText();
            checkPassword(login, password);
        }
    }

    public void emailInputHande(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String email = emailInput.getText();
            checkEmail(email);
        }
    }

}