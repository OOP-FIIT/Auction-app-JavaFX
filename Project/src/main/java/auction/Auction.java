package auction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import auction.controllers.User;
import auction.dataBase.SQL;
import auction.exception.BidException;
import auction.interfaces.License;
import auction.shared.Const;
import auction.threads.SendLicenseEmail;
import auction.threads.UpdateLicense;

/**
 * The type Auction.
 */
public class Auction extends AbstractModel{
    private static int userId = 0;
    private static UserData currentUser;
    private static boolean endAuctionFirstClick = false;

    /**
     * The Message.
     */
    static License message;


    /**
     * Try add bid boolean.
     *
     * @param bidStr the bid str
     * @param lotId  the lot id
     * @return boolean boolean
     * @throws SQLException the sql exception
     * @throws BidException the bid exception
     */
    public static boolean tryAddBid(String bidStr, int lotId) throws SQLException, BidException {
        if (bidStr.equals("")) {
            throw new BidException("You cannot add [EMPTY] bid");
        } else if (bidStr.equals("0")) {
            throw new BidException("You cannot add [0] bid");
        } else {
            int bid = Integer.parseInt(bidStr);
            UserData user = new UserData(userId);
            if (user.getBalance() < bid)
                return false;
            else {
                SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datenow = new java.util.Date();
                String date = DATETIME.format(datenow);
                SQL.InsertBid(userId, date, lotId, bid);
                SQL.UPDATE_User(null, null, null, null, -bid, true, userId);
                return true;
            }
        }
        // get userData from SQL
        // check if balance is enough to make a bid
    }


    /**
     * End auction.
     *
     * @param lotId the lot id
     * @throws SQLException the sql exception
     */
    public static void endAuction(int lotId) throws SQLException {
        // SQL get all bids
        int winningBid;
        int bid;
        int buyerId;
        ResultSet res = SQL.SELECT_Bids(lotId);
        int sellerId = SQL.SELECT_SellerIdByLotId(lotId);

        // Return bid to first buyer
        if (!res.next()) {
            if (!endAuctionFirstClick) {
                endAuctionFirstClick = true;
                System.out.println(
                        "You`ve tried to finish auction without bids, click button one more time to delete this Lot");
                return;
            } else {
                System.out.println("Lot with id: " + lotId + " ENDED--");
                endAuctionFirstClick = false;
                SQL.DELETE_Lot(lotId);
                SQL.DELETE_Bids(lotId);
                return;
            }
        }

        winningBid = res.getInt(Const.SQL.BIDS_BID);
        bid = res.getInt(Const.SQL.BIDS_BID);
        buyerId = res.getInt(Const.SQL.BIDS_BUYER_ID);
        SQL.UPDATE_User(null, null, null, null, bid, true, buyerId);

        // If only 1 bid - then it wins
        if (!res.next()) {
            System.out.println("You finished Auction with only 1 bid");
            // Set 1 bid as winner
            SQL.UPDATE_User(null, null, null, null, -winningBid, true, buyerId);
            // Give money to seller
            SQL.UPDATE_User(null, null, null, null, winningBid, true, sellerId);
            SQL.DELETE_Lot(lotId);
            SQL.DELETE_Bids(lotId);
            
            ResultSet winnerUser = SQL.SELECT_UserData(buyerId);
            winnerUser.next();
            ResultSet sellerUser = SQL.SELECT_UserData(sellerId);
            sellerUser.next();

            sendWinEmail(winnerUser.getString(Const.SQL.USERDATA_EMAIL), String.valueOf(lotId), sellerUser.getString(Const.SQL.USERDATA_LOGIN));
            setEndAuctionFirstClick(false);
            return;

        }

        winningBid = res.getInt(Const.SQL.BIDS_BID);
        ResultSet winnerUser = SQL.SELECT_UserData(res.getInt("buyer_id"));
        winnerUser.next();
        ResultSet sellerUser = SQL.SELECT_UserData(sellerId);
        sellerUser.next();

        sendWinEmail(winnerUser.getString(Const.SQL.USERDATA_EMAIL), String.valueOf(lotId), sellerUser.getString(Const.SQL.USERDATA_LOGIN));
        setEndAuctionFirstClick(false);

        while (res.next()) {
            SQL.UPDATE_User(null, null, null, null, res.getInt("bid"), true, res.getInt("buyer_id"));
        }
        SQL.UPDATE_User(null, null, null, null, winningBid, true, sellerId);
        SQL.DELETE_Lot(lotId);
        SQL.DELETE_Bids(lotId);
        setEndAuctionFirstClick(false);

        // Take 2nd the most bid\
        // Return money to losers,
        // give lot to winner,
        // add money to seller
        // delete lot
    }

    /**
     * Gets user.
     *
     * @return the uSER
     */
    public static UserData getUSER() {
        return currentUser;
    }

    /**
     * Update user.
     *
     * @throws SQLException the sql exception
     */
    public static void updateUser() throws SQLException {
        setUSER(new UserData(userId));
    }


    /**
     * Sets user.
     *
     * @param uSER the u ser
     */
    public static void setUSER(UserData uSER) {
        currentUser = uSER;
    }

    /**
     * Gets user id.
     *
     * @return the uSER_ID
     */
    public static int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param newUserId the new user id
     */
    public static void setUserId(int newUserId) {
        userId = newUserId;
    }

    /**
     * Is end auction first click boolean.
     *
     * @return the endAuctionFirstClick
     */
    public static boolean isEndAuctionFirstClick() {
        return endAuctionFirstClick;
    }

    /**
     * Sets end auction first click.
     *
     * @param state the state
     */
    public static void setEndAuctionFirstClick(boolean state) {
        endAuctionFirstClick = state;
    }

    /**
     * Creates LicenseKey
     * Updates DataBase
     * Switches window to PRO mode
     *
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws SQLException             the sql exception
     * @throws IOException              the io exception
     */
    public static void setLicenseKey() throws NoSuchAlgorithmException, SQLException, IOException {
        String licenseKey = generateLicenseKey();
        Thread sendLicenseThread = new SendLicenseEmail(licenseKey, currentUser.getEmail(), currentUser.getLogin());
        sendLicenseThread.start();        
        //Update license record in SQL 
        Thread updateLicense = new UpdateLicense(licenseKey, currentUser.getId());
        updateLicense.start();
        App.changeScene(Const.FXML.AUCTION_SCENE, new User());
    }

    /**
     * Returns LicenseKey depends on user`s login, id, email
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String generateLicenseKey() throws NoSuchAlgorithmException {
        setEncryprionMessage();
        String tohash = message.encryprionMessage(String.valueOf(currentUser.getId()), currentUser.getEmail(), currentUser.getLogin());
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(tohash.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    /**
     * Here we specify how our message will be created
     * message is a text, that will be hashed and set as License
     * so it is importand to set some algorithm that combines some parts ow information and returns message
     * 
     * right now here I have used simple concatenation, but it could also be like: 
     * arg1 + arg1.lenght()/2 + arg2.substring(3) + arg3.charAt(10);
     */
    private static void setEncryprionMessage(){
        message = (arg1, arg2, arg3)-> {
            return arg1 + arg2 + arg3;
        };
    }
    
    /**
     * Returns String from byte[] hash 
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Return TRUE if license is correct and FALSE if it differs from DataBase`s license
     *
     * @param file the file
     * @return boolean boolean
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public static boolean verifyLicense(File file) throws SQLException, IOException {
        updateUser();
        String licenseKey = getLicenseFromJSON(file);
        if(licenseKey == null)
            return false;

            return licenseKey.equals(currentUser.getLicense());
    }

    /**
     * Returns String with LicenseKey from JSON file
     * @param file - if null then it will search file near the programm
     * @return
     * @throws FileNotFoundException
     */
    private static String getLicenseFromJSON(File file) throws FileNotFoundException{
        String path = currentUser.getLogin() + ".json";
        if(file != null)
            path = file.getPath();
            
        JSONParser jsonParser = new JSONParser();
        String licenseKey = "";
        try {
            JSONObject licenseKeyJSON = (JSONObject) jsonParser.parse(new FileReader(path));
             licenseKey = (String) licenseKeyJSON.get("key");
        } catch (IOException|ParseException e) {
            System.out.println("Cannot find license near app, please try add license manually");
        }

        return licenseKey;
    }

    /**
     * Sends email to new PRO version owner with licenseKey.JSON file
     *
     * @param reciever the reciever
     * @return the int
     */
    public static int sendActivationMail(String reciever) {
        int activationCode = -1;
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
            Random rand = new Random();
            // Set Subject: header field
            activationCode = rand.nextInt(100000);
            message.setSubject("Code: " + activationCode);

            message.setText(Const.MAIL.getCodeMessage(activationCode));

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return activationCode;
    }

    /**
     * Sends Email about winnind in Auction
     * @param reciever
     * @param lotName
     * @param sellerName
     */
    public static void sendWinEmail(String reciever, String lotName, String sellerName){
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
            // Set Subject: header field
            message.setSubject("You won in auction...");

            message.setText(Const.MAIL.getWinMessage(lotName, sellerName));

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    } 

}
