package auction.shared;

public final class Const {

    public static final class SQL{
        public static final String DB_URL = "jdbc:mysql://localhost/";
        public static final String DB_LOGIN = "root";
        public static final String DB_PASS = "misha";
        public static final String DB_NAME = "AUCTION";
        
        public static final String USERDATA = "USERDATA";     
            public static final String USERDATA_ID = "id";     
            public static final String USERDATA_LOGIN = "login";    
            public static final String USERDATA_PASSWORD = "password";    
            public static final String USERDATA_EMAIL = "email";     
            public static final String USERDATA_MODE = "mode";     
            public static final String USERDATA_BALANCE = "balance";     
            public static final String USERDATA_LICENSE = "license";     
        
        
        public static final String LOTS = "Lots";
            public static final String LOTS_ID = "id";
            public static final String LOTS_SELLER_ID = "seller_id";
            public static final String LOTS_NAME = "name";
            public static final String LOTS_DATE = "date";
            public static final String LOTS_DESCRIPTION = "description";
        
        
        public static final String BIDS = "Bid";
            public static final String BIDS_ID = "id";
            public static final String BIDS_BUYER_ID = "buyer_id";
            public static final String BIDS_DATE = "date";
            public static final String BIDS_LOT_ID = "lot_id";
            public static final String BIDS_BID = "bid";
        
        public static final String USER_MODE_BUYER = "Buyer";
        public static final String USER_MODE_SELLER = "Seller";
        public static final String USER_MODE_AUCTIONER = "Auctioner";
        public static final String USER_MODE_PRO = "Pro";
    }


    public static final class FXML{
        public static final String LOGIN_SCENE = "auth";
        public static final String AUCTION_SCENE = "menu";
        public static final String BUY_PRO_WINDOW = "BUYPRO";

        protected static final String[] PRO_TIPS = {
            "DoubleClick on (End Auction) deletes Lot even if there was no bids" ,
        };


    }

    public static final class MAIL{
        public static final String LOGIN = "sichkaruk.company@gmail.com";
        public static final String PASSWORD = "Company_2001";
        public static final String HOST = "smtp.gmail.com";
        public static final String getCodeMessage(int activationCode){
           return "Wake up, Neo..."                     + " \n" + 
                  "The Matrix has you..."               + " \n" + 
                  "Follow the code: " + activationCode  + " \n\n" + 
                  "Knock, Knock, Neo.";
        }
        

    }

}
