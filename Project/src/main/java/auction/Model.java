package auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Model {
    private static int USER_ID = 0;
    private static UserData USER;

    public static class UserData {
        private int id;
        private String login;
        private String email;
        private int balance;

        public UserData(int userId) throws SQLException{
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

    public static boolean tryAddBid(int bid, int lotId) throws SQLException {
        UserData user = new UserData(USER_ID);
        System.out.println(user.balance);
        if(user.getBalance() < bid)
            return false;
        else{
            System.out.println("Id : " + USER_ID);
            SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datenow = new java.util.Date();
            String date = DATETIME.format(datenow);
            SQL.InsertBid(USER_ID, date, lotId);
            SQL.UPDATE_User(null, null, null, null, user.balance + 10, USER_ID);
            return true;
        }
        //get userData from SQL
        //check if balance is enough to make a bid
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

    public static void UpdateUser() throws SQLException{
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

}
