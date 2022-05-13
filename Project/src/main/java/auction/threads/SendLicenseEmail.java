package auction.threads;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.json.simple.JSONObject;

import auction.shared.Const;

public class SendLicenseEmail extends Thread{
    private String licenseKey;
    private String email;
    private String login;

    public SendLicenseEmail(String license, String email, String login){
        this.licenseKey = license; 
        this.email = email;
        this.login = login;
    } 

    /**
     * Sends email while App is switching to PRO and while License record in SQL is updating
     */
    @Override
    public void run(){
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
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.email));
            // Set Subject: header field
            message.setSubject("License Key");

            message.setText(licenseKey);
            System.out.println(licenseKey);
            System.out.println("sending...");
            // Send message
            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            createJSON();
            // Part two is attachment
            MimeBodyPart messageBodyPart = new MimeBodyPart();
  
            try {
                messageBodyPart.attachFile(this.login + ".json");
                messageBodyPart.setFileName(this.login + ".json");

            } catch (Exception e) {
                e.printStackTrace();
            }
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private void createJSON(){
        JSONObject licenseJSON = new JSONObject();
        licenseJSON.put("key", licenseKey);
        licenseJSON.put("login", login);
        try {
            FileWriter file = new FileWriter(this.login + ".json");
            file.write(licenseJSON.toJSONString());
            System.out.println("file writed");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
