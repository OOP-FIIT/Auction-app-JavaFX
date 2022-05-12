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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import auction.controllers.User;
import auction.exception.BidException;
import auction.shared.Const;
import auction.sql.SQL;
import auction.threads.SendLicenseEmail;
import auction.threads.UpdateLicense;

public class Model {
    private static int userId = 0;
    private static UserData currentUser;
    private static boolean endAuctionFirstClick = false;

    public static class UserData {
        private int id;
        private String login;
        private String email;
        private int balance;
        private String mode;
        private String license;

        public UserData(int userId) throws SQLException {
            ResultSet res = SQL.SELECT_UserData(userId);
            res.next();
            this.login = res.getString(Const.SQL.USERDATA_LOGIN);
            this.balance = res.getInt(Const.SQL.USERDATA_BALANCE);
            this.email = res.getString(Const.SQL.USERDATA_EMAIL);
            this.mode = res.getString(Const.SQL.USERDATA_MODE);
            this.license = res.getString(Const.SQL.USERDATA_LICENSE);
            this.id = userId;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * @return the login
         */
        public String getLogin() {
            return login;
        }

        /**
         * @param login the login to set
         */
        public void setLogin(String login) {
            this.login = login;
        }

        /**
         * @return the email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email the email to set
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * @return the balance
         */
        public int getBalance() {
            return balance;
        }

        /**
         * @param balance the balance to set
         */
        public void setBalance(int balance) {
            this.balance = balance;
        }

        /**
         * @return the mode
         */
        public String getMode() {
            return mode;
        }

        /**
         * @param mode the mode to set
         */
        public void setMode(String mode) {
            this.mode = mode;
        }

        /**
         * @return the license
         */
        public String getLicense() {
            return license;
        }

        /**
         * @param license the license to set
         */
        public void setLicense(String license) {
            this.license = license;
        }
    }

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

    public static void endAuction(int lotId) throws SQLException {
        // SQL get all bids
        int winningBid;
        int bid;
        int buyerId;
        ResultSet res = SQL.SELECT_Bids(lotId);

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
            int sellerId = SQL.SELECT_SellerIdByLotId(lotId);
            // Set 1 bid as winner
            SQL.UPDATE_User(null, null, null, null, -winningBid, true, bid);
            // Give money to seller
            SQL.UPDATE_User(null, null, null, null, winningBid, true, sellerId);
            SQL.DELETE_Lot(lotId);
            SQL.DELETE_Bids(lotId);

            setEndAuctionFirstClick(false);
            return;

        }

        winningBid = res.getInt(Const.SQL.BIDS_BID);
        SQL.UPDATE_User(null, null, null, null, 0, true, res.getInt("buyer_id"));

        while (res.next()) {
            SQL.UPDATE_User(null, null, null, null, res.getInt("bid"), true, res.getInt("buyer_id"));
        }
        int sellerId = SQL.SELECT_SellerIdByLotId(lotId);
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
     * @return the uSER
     */
    public static UserData getUSER() {
        return currentUser;
    }

    /**
     * @param uSER the uSER to set
     */
    public static void setUSER(UserData uSER) {
        currentUser = uSER;
    }

    public static void updateUser() throws SQLException {
        setUSER(new UserData(userId));
    }

    /**
     * @return the uSER_ID
     */
    public static int getUserId() {
        return userId;
    }

    /**
     * @param userId the uSER_ID to set
     */
    public static void setUserId(int newUserId) {
        userId = newUserId;
    }

    /**
     * @return the endAuctionFirstClick
     */
    public static boolean isEndAuctionFirstClick() {
        return endAuctionFirstClick;
    }

    /**
     * @param endAuctionFirstClick the endAuctionFirstClick to set
     */
    public static void setEndAuctionFirstClick(boolean state) {
        endAuctionFirstClick = state;
    }

    public static void setLicenseKey() throws NoSuchAlgorithmException, SQLException, IOException {
        String licenseKey = generateLicenseKey();
        Thread sendLicenseThread = new SendLicenseEmail(licenseKey, currentUser.getEmail(), currentUser.getLogin());
        sendLicenseThread.start();        
        //Update license record in SQL 
        Thread updateLicense = new UpdateLicense(licenseKey, currentUser.getId());
        updateLicense.start();
        App.changeScene(Const.FXML.AUCTION_SCENE, new User());
    }


    private static String generateLicenseKey() throws NoSuchAlgorithmException {
        String tohash = currentUser.getId() + currentUser.getEmail() + currentUser.getLogin();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(tohash.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

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

    public static boolean verifyLicense(File file) throws SQLException, IOException {
        updateUser();
        String licenseKey = getLicenseFromJSON(file);
        if(licenseKey == null)
            return false;

            return licenseKey.equals(currentUser.getLicense());
    }

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


}
