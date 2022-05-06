package auction.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javafx.application.Platform;
import javafx.event.EventHandler;
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
import auction.SQL.SQL;
import auction.shared.Const;

public class PrimaryController {
    private Random rand;
    private int activationCode;
    private boolean sing_in_password = true;
    private String save_password_1_try = "";
    private boolean password_1_try = false;
    private boolean email_input = true;

    private String password_final;
    private String login_final;
    private String email_final;
    private int balance_final = 100;
    @FXML
    private TextField login_input_mainScene;
    @FXML
    private TextField password_input_mainScene;
    @FXML
    private TextField email_input_mainScene;
    @FXML
    private Text login_text_mainScene;
    @FXML
    private Text password_text_mainScene;
    @FXML
    private Text email_text_mainScene;
    @FXML
    private GridPane user_modes_grid;

    public void initialize() {
        Platform.runLater(() -> login_input_mainScene.requestFocus());
    }

    private void handle_login(String login) throws IOException, SQLException {
        if (SQL.IsLoginExists(login)) {
            sing_in_password = true;
            sing_in(login);
        } else {
            sing_in_password = false;
            sign_up(login);
        }
    }

    private void handle_password(String login, String password) throws SQLException, IOException {
        if (sing_in_password) {
            int userID = SQL.IsPaaswordCorrect(login, password);
            Model.setUSER_ID(userID);
            Model.UpdateUser();
            String mode =  Model.getUSER().getMode();
            if (userID != 0)
                switchToMenu(mode, userID);
            else {
                password_input_mainScene.setText("");
                password_text_mainScene.setText("Please, try again");
            }
        } else {
            if (!password_1_try) {
                save_password_1_try = password;
                password_1_try = true;
                password_input_mainScene.setText("");
                password_text_mainScene.setText("Please, repeat password");
                Platform.runLater(() -> password_input_mainScene.requestFocus());
            } else {
                if (save_password_1_try.equals(password)) {
                    password_input_mainScene.setText("");
                    password_text_mainScene.setText("Done!");
                    email_input_mainScene.setVisible(true);
                    email_text_mainScene
                            .setStyle("visibility: visible; -fx-background-color: grey; alligment: center; ");
                    password_final = password;
                    password_input_mainScene.setDisable(true);
                    Platform.runLater(() -> email_input_mainScene.requestFocus());
                } else {
                    save_password_1_try = "";
                    password_1_try = false;
                    password_input_mainScene.setText("");
                    password_text_mainScene.setText("Create password for account");
                    Platform.runLater(() -> password_input_mainScene.requestFocus());
                }
            }
        }
    }

    private void sing_in(String login) {
        password_text_mainScene.setVisible(true);

        password_input_mainScene.setVisible(true);
        Platform.runLater(() -> password_input_mainScene.requestFocus());
    }

    private void sign_up(String login) {
        password_text_mainScene.setVisible(true);
        password_input_mainScene.setVisible(true);
        password_text_mainScene.setText("Create password for account");
        login_text_mainScene.setText("Great name");
        login_final = login;
        login_input_mainScene.setDisable(true);
        Platform.runLater(() -> password_input_mainScene.requestFocus());
    }

    public void handle_login_input(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = login_input_mainScene.getText();
            handle_login(login);
            System.out.println("login: " + login);
        }
    }

    public void handle_password_input(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = login_input_mainScene.getText();
            String password = password_input_mainScene.getText();
            handle_password(login, password);
        }
    }

    public void handle_email_input(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = login_input_mainScene.getText();
            String password = password_input_mainScene.getText();
            String email = email_input_mainScene.getText();
            handle_email(login, password, email);
        }
    }

    private void handle_email(String login, String password, String email) {
        if (email_input == true) {
            send_activation_mail(email);
            email_text_mainScene.setText("Super, now just put your verification code here");
            email_input_mainScene.clear();
            ;
            email_input = false;

            email_final = email;
            rand = new Random();
            balance_final = rand.nextInt(100000);
        } else {
            if (Integer.parseInt(email) == activationCode) {
                email_text_mainScene.setText("Beautiful, last thing to do - choose yours account mode");
                user_modes_grid.setVisible(true);

                email_input_mainScene.setDisable(true);
            } else {

            }
        }
    }

    private void send_activation_mail(String reciever) {



        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", Const.MAIL.HOST);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

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

            message.setText(Const.MAIL.MESSAGE(activationCode));

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    private void switchToMenu(String mode, int userID) throws IOException {
        if (mode.equals(Const.SQL.USER_MODE_PRO)) {
            Model.setUSER_ID(userID);
            App.changeScene(Const.FXML.AUCTION_SCENE, new User());
        } else if (mode.equals(Const.SQL.USER_MODE_AUCTIONER)) {
            Model.setUSER_ID(userID);
            App.changeScene(Const.FXML.AUCTION_SCENE, new Auctioner());        
        } else if (mode.equals(Const.SQL.USER_MODE_BUYER)) {
            Model.setUSER_ID(userID);
            App.changeScene(Const.FXML.AUCTION_SCENE, new Buyer());
        } else if (mode.equals(Const.SQL.USER_MODE_SELLER)) {
            Model.setUSER_ID(userID);
            App.changeScene(Const.FXML.AUCTION_SCENE, new Seller());
        }

    }

    public void handle_buyer_button() throws SQLException, IOException {
        int userID = SQL.InsertUser(login_final, password_final, email_final, Const.SQL.USER_MODE_BUYER, balance_final);
        switchToMenu(Const.SQL.USER_MODE_PRO, userID);
    }

    public void handle_seller_button() throws SQLException, IOException {
        int userID = SQL.InsertUser(login_final, password_final, email_final, Const.SQL.USER_MODE_SELLER, balance_final);
        switchToMenu(Const.SQL.USER_MODE_SELLER, userID);
    }

    public void handle_auctioner_button() throws SQLException, IOException {
        int userID = SQL.InsertUser(login_final, password_final, email_final, Const.SQL.USER_MODE_AUCTIONER, balance_final);
        switchToMenu(Const.SQL.USER_MODE_PRO, userID);
    }



}