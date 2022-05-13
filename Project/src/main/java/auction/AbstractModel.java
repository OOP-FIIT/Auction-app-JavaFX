package auction;

import java.sql.ResultSet;
import java.sql.SQLException;

import auction.shared.Const;
import auction.sql.SQL;

public class AbstractModel {
    private static UserData currentUser;

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

    public static void updateUser(int newUserId) throws SQLException {
        setUSER(new UserData(newUserId));
    }
    
    public static void setUSER(UserData uSER) {
        currentUser = uSER;
    }
}
