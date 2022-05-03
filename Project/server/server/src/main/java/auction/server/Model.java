package auction.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Model {
    private String[] ProTip = { "DoubleClick on (End Auction) deletes Lot even if there was no bids" };
    private static int USER_ID = 0;
    private static UserData USER;
    private static boolean EndAuctionFirstClick = false;

    public static class UserData {
        private int id;
        private String login;
        private String email;
        private int balance;

        public UserData(int userId) throws SQLException {
            ResultSet res = SQL.SELECT_UserData(userId);
            res.next();
            this.login = res.getString("login");
            this.balance = res.getInt("balance");
            this.email = res.getString("email");
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

    }

    public static boolean tryAddBid(String bidStr, int lotId) throws SQLException, BidException {
        if (bidStr.equals("")) {
            throw new BidException("You cannot add [EMPTY] bid");
        } else if (bidStr.equals("0")) {
            throw new BidException("You cannot add [0] bid");
        } else {

            int bid = Integer.parseInt(bidStr);
            UserData user = new UserData(USER_ID);
            System.out.println(user.balance);
            if (user.getBalance() < bid)
                return false;
            else {
                System.out.println("Id : " + USER_ID);
                SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datenow = new java.util.Date();
                String date = DATETIME.format(datenow);
                SQL.InsertBid(USER_ID, date, lotId, bid);
                SQL.UPDATE_User(null, null, null, null, -bid, true, USER_ID);
                return true;
            }
        }
        // get userData from SQL
        // check if balance is enough to make a bid
    }

    public static void EndAuction(int lotId) throws SQLException {
        // SQL get all bids
        int winningBid, bid, buyer_id;
        ResultSet res = SQL.SELECT_Bids(lotId);

        // Return bid to first buyer
        if (!res.next()) {
            if (!EndAuctionFirstClick) {
                EndAuctionFirstClick = true;
                System.out.println(
                        "You`ve tried to finish auction without bids, click button one more time to delete this Lot");
                return;
            } else {
                EndAuctionFirstClick = false;
                SQL.DELETE_Lot(lotId);
                SQL.DELETE_Bids(lotId);
                return;
            }
        }

        winningBid = res.getInt("bid");
        bid = res.getInt("bid");
        buyer_id = res.getInt("buyer_id");
        SQL.UPDATE_User(null, null, null, null, bid, true, buyer_id);
        System.out.println("bid = " + res.getInt("bid") + "\t buyer: " + res.getInt("buyer_id"));

        // If only 1 bid - then it wins
        if (!res.next()) {
            System.out.println("You finished Auction with only 1 bid");
            int sellerId = SQL.SELECT_SellerIdByLotId(lotId);
            // Set 1 bid as winner
            SQL.UPDATE_User(null, null, null, null, -winningBid, true, bid);
            // Give money to seller
            SQL.UPDATE_User(null, null, null, null, winningBid, true, sellerId);
            // Delete Lot and related bids;
            SQL.DELETE_Lot(lotId);
            SQL.DELETE_Bids(lotId);

            setEndAuctionFirstClick(false);
            return;

        }

        winningBid = res.getInt("bid");
        System.out.println("bid = " + res.getInt("bid") + "\t buyer: " + res.getInt("buyer_id"));
        SQL.UPDATE_User(null, null, null, null, 0, true, res.getInt("buyer_id"));

        while (res.next()) {
            SQL.UPDATE_User(null, null, null, null, res.getInt("bid"), true, res.getInt("buyer_id"));
            System.out.println("bid = " + res.getInt("bid") + "\t buyer: " + res.getInt("buyer_id"));

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
        return USER;
    }

    /**
     * @param uSER the uSER to set
     */
    public static void setUSER(UserData uSER) {
        USER = uSER;
    }

    public static void UpdateUser() throws SQLException {
        setUSER(new UserData(USER_ID));
    }

    /**
     * @return the uSER_ID
     */
    public static int getUSER_ID() {
        return USER_ID;
    }

    /**
     * @param uSER_ID the uSER_ID to set
     */
    public static void setUSER_ID(int uSER_ID) {
        USER_ID = uSER_ID;
    }

    /**
     * @return the endAuctionFirstClick
     */
    public static boolean isEndAuctionFirstClick() {
        return EndAuctionFirstClick;
    }

    /**
     * @param endAuctionFirstClick the endAuctionFirstClick to set
     */
    public static void setEndAuctionFirstClick(boolean endAuctionFirstClick) {
        EndAuctionFirstClick = endAuctionFirstClick;
    }

}
