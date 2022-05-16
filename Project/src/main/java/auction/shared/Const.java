package auction.shared;

/**
 * Constants class
 */
public final class Const {

    /**
     * The type Sql.
     */
    public static final class SQL{
        /**
         * The constant DB_URL.
         */
        public static final String DB_URL = "jdbc:mysql://localhost/";
        /**
         * The constant DB_LOGIN.
         */
        public static final String DB_LOGIN = "root";
        /**
         * The constant DB_PASS.
         */
        public static final String DB_PASS = "misha";
        /**
         * The constant DB_NAME.
         */
        public static final String DB_NAME = "AUCTION";

        /**
         * The constant USERDATA.
         */
        public static final String USERDATA = "USERDATA";
        /**
         * The constant USERDATA_ID.
         */
        public static final String USERDATA_ID = "id";
        /**
         * The constant USERDATA_LOGIN.
         */
        public static final String USERDATA_LOGIN = "login";
        /**
         * The constant USERDATA_PASSWORD.
         */
        public static final String USERDATA_PASSWORD = "password";
        /**
         * The constant USERDATA_EMAIL.
         */
        public static final String USERDATA_EMAIL = "email";
        /**
         * The constant USERDATA_MODE.
         */
        public static final String USERDATA_MODE = "mode";
        /**
         * The constant USERDATA_BALANCE.
         */
        public static final String USERDATA_BALANCE = "balance";
        /**
         * The constant USERDATA_LICENSE.
         */
        public static final String USERDATA_LICENSE = "license";


        /**
         * The constant LOTS.
         */
        public static final String LOTS = "Lots";
        /**
         * The constant LOTS_ID.
         */
        public static final String LOTS_ID = "id";
        /**
         * The constant LOTS_SELLER_ID.
         */
        public static final String LOTS_SELLER_ID = "seller_id";
        /**
         * The constant LOTS_NAME.
         */
        public static final String LOTS_NAME = "name";
        /**
         * The constant LOTS_DATE.
         */
        public static final String LOTS_DATE = "date";
        /**
         * The constant LOTS_DESCRIPTION.
         */
        public static final String LOTS_DESCRIPTION = "description";


        /**
         * The constant BIDS.
         */
        public static final String BIDS = "Bid";
        /**
         * The constant BIDS_ID.
         */
        public static final String BIDS_ID = "id";
        /**
         * The constant BIDS_BUYER_ID.
         */
        public static final String BIDS_BUYER_ID = "buyer_id";
        /**
         * The constant BIDS_DATE.
         */
        public static final String BIDS_DATE = "date";
        /**
         * The constant BIDS_LOT_ID.
         */
        public static final String BIDS_LOT_ID = "lot_id";
        /**
         * The constant BIDS_BID.
         */
        public static final String BIDS_BID = "bid";

        /**
         * The constant USER_MODE_BUYER.
         */
        public static final String USER_MODE_BUYER = "Buyer";
        /**
         * The constant USER_MODE_SELLER.
         */
        public static final String USER_MODE_SELLER = "Seller";
        /**
         * The constant USER_MODE_AUCTIONER.
         */
        public static final String USER_MODE_AUCTIONER = "Auctioner";
        /**
         * The constant USER_MODE_PRO.
         */
        public static final String USER_MODE_PRO = "Pro";
    }


    /**
     * The type Fxml.
     */
    public static final class FXML{
        /**
         * The constant LOGIN_SCENE.
         */
        public static final String LOGIN_SCENE = "auth";
        /**
         * The constant AUCTION_SCENE.
         */
        public static final String AUCTION_SCENE = "menu";
        /**
         * The constant BUY_PRO_WINDOW.
         */
        public static final String BUY_PRO_WINDOW = "BUYPRO";

        /**
         * The constant PRO_TIPS.
         */
        protected static final String[] PRO_TIPS = {
            "DoubleClick on (End Auction) deletes Lot even if there was no bids" ,
        };


    }

    /**
     * The type Mail.
     */
    public static final class MAIL{
        /**
         * The constant LOGIN.
         */
        public static final String LOGIN = "sichkaruk.company@gmail.com";
        /**
         * The constant PASSWORD.
         */
        public static final String PASSWORD = "Company_2001";
        /**
         * The constant HOST.
         */
        public static final String HOST = "smtp.gmail.com";

        /**
         * Get code message string.
         *
         * @param activationCode the activation code
         * @return the string
         */
        public static final String getCodeMessage(int activationCode){
           return "Wake up, Neo..."                     + " \n" + 
                  "The Matrix has you..."               + " \n" + 
                  "Follow the code: " + activationCode  + " \n\n" + 
                  "Knock, Knock, Neo.";
        }

        public static final String getWinMessage(String lotName, String sellerName){
           return "Congratulations!"                     + " \n" + 
                  "You have won in auction (seller: "+ sellerName + ")"               + " \n" + 
                  "Your new item: " + lotName  ;
        }
        

    }

}
