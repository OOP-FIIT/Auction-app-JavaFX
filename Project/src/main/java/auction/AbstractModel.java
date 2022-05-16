package auction;

import java.sql.ResultSet;
import java.sql.SQLException;

import auction.dataBase.SQL;
import auction.shared.Const;

/**
 * Model of Auction that can be used to implement own Auction type
 */
public class AbstractModel {
    private static UserData currentUser;

    /**
     * Nested Class that will be used in Model to save User`s data in a Obbject
     */
    public static class UserData {
        private int id;
        private String login;
        private String email;
        private int balance;
        private String mode;
        private String license;

        /**
         * Instantiates a new User data.
         *
         * @param userId the user id
         * @throws SQLException the sql exception
         */
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
         * Gets id.
         *
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        private void setId(int id) {
            this.id = id;
        }

        /**
         * Gets login.
         *
         * @return the login
         */
        public String getLogin() {
            return login;
        }

        /**
         * @param login the login to set
         */
        private void setLogin(String login) {
            this.login = login;
        }

        /**
         * Gets email.
         *
         * @return the email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email the email to set
         */
        private void setEmail(String email) {
            this.email = email;
        }

        /**
         * Gets balance.
         *
         * @return the balance
         */
        public int getBalance() {
            return balance;
        }

        /**
         * @param balance the balance to set
         */
        private void setBalance(int balance) {
            this.balance = balance;
        }

        /**
         * Gets mode.
         *
         * @return the mode
         */
        public String getMode() {
            return mode;
        }

        /**
         * @param mode the mode to set
         */
        private void setMode(String mode) {
            this.mode = mode;
        }

        /**
         * Gets license.
         *
         * @return the license
         */
        public String getLicense() {
            return license;
        }

        /**
         * @param license the license to set
         */
        private void setLicense(String license) {
            this.license = license;
        }
    }


    /**
     * Update user.
     *
     * @param newUserId the new user id
     * @throws SQLException the sql exception
     */
    public static void updateUser(int newUserId) throws SQLException {
        setUSER(new UserData(newUserId));
    }


    /**
     * Sets user.
     *
     * @param uSER the u ser
     */
    public static void setUSER(UserData uSER) {
        currentUser = uSER;
    }
}
