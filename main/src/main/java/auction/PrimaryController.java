package auction;

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

public class PrimaryController {
    private Random rand;
    private int activation_code;
    private boolean sing_in_password = true;
    private String save_password_1_try = "";
    private boolean password_1_try = false;
    private boolean email_input = true;
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

    @FXML
    public void initialize() {
        Platform.runLater(() -> login_input_mainScene.requestFocus());
    }

    @FXML
    private void handle_login(String login) throws IOException, SQLException {
        if (Sql_manager.login_exists(login)) {
            sing_in_password = true;
            sing_in(login);
        } else {
            sing_in_password = false;
            sign_up(login);
        }
    }

    @FXML
    private void sign_up(String login) {
        password_text_mainScene.setStyle("visibility: visible; -fx-background-color: grey; alligment: center;");
        password_input_mainScene.setStyle("visibility: visible; -fx-background-color: grey");
        password_text_mainScene.setText("Create password for account");
        login_text_mainScene.setText("Great name");
        Platform.runLater(() -> password_input_mainScene.requestFocus());
    }

    @FXML
    private void handle_password(String login, String password) throws SQLException, IOException {
        if (sing_in_password == true) {
            if (Sql_manager.password_correct(login, password))
                signed_in(login, password);
            else
                repeat_password();
        } else {
            if (password_1_try == false) {
                save_password_1_try = password;
                password_1_try = true;
                password_input_mainScene.setText("");
                password_text_mainScene.setText("Please, repeat password");
            } else {
                if (save_password_1_try.equals(password)) {
                    password_input_mainScene.setText("");
                    password_text_mainScene.setText("Done!");
                    email_input_mainScene
                            .setStyle("visibility: visible; -fx-background-color: grey; alligment: center; ");
                    email_text_mainScene
                            .setStyle("visibility: visible; -fx-background-color: grey; alligment: center; ");
                    Platform.runLater(() -> email_input_mainScene.requestFocus());
                } else {
                    save_password_1_try = "";
                    password_1_try = false;
                    password_input_mainScene.setText("");
                    password_text_mainScene.setText("Create password for account");
                }
            }
        }
    }

    @FXML
    private void repeat_password() {
        password_input_mainScene.setText("");
        password_text_mainScene.setText("Please, try again");
    }

    @FXML
    private void sing_in(String login) {
        password_text_mainScene
                .setStyle("visibility: visible; -fx-background-color: grey; alligment: center;");

        password_input_mainScene.setStyle("visibility: visible; -fx-background-color: grey");
        password_input_mainScene.requestFocus();

    }

    @FXML
    private void signed_in(String login, String password) {
        // App.setRoot("main_menu");
        System.out.println("YOU SIGNED IN!!!!" + login + " " + password);
    }

    @FXML
    public void handle_login_input(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = login_input_mainScene.getText();
            handle_login(login);
            System.out.println("login: " + login);
        }
    }

    @FXML
    public void handle_password_input(KeyEvent ke) throws SQLException, IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String login = login_input_mainScene.getText();
            String password = password_input_mainScene.getText();
            handle_password(login, password);
        }
    }

    @FXML
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
            email_input = false;
        } else {
            if (Integer.parseInt(email) == activation_code)
                email_text_mainScene.setText("Beautiful, last thing to do - choose yours account mode");
            user_modes_grid.setStyle("visibility: visible");
        }
    }

    @FXML
    private void send_activation_mail(String reciever) {

        String mail_sender = "sichkaruk.company@gmail.com";
        String mail_password = "Company_2001";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail_sender, mail_password);
            }
        });

        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(mail_sender));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(reciever));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");
            rand = new Random();
            activation_code = rand.nextInt(100000);
            message.setText("Wake up, Neo...\nThe Matrix has you...\nFollow the code: " + activation_code
                    + "\n\n Knock, Knock, Neo.");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
}
