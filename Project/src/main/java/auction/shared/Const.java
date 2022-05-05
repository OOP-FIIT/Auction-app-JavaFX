package auction.shared;

public final class Const {
    public static final String DB_URL = "jdbc:mysql://localhost/";
    public static final String DB_LOGIN = "root";
    public static final String DB_PASS = "misha";

    public static final String DB_NAME = "AUCTION";
    
    public static final String USERDATA = "USERDATA";     
        public static final String USERDATA_ID = "id";     
        public static final String USERDATA_LOGIN = "login";    
        public static final String USERDATA_PASSWORD = "password";    
        public static final String USERDATA_EMAIL = "email";     
        public static final String USERDATA_MODD = "modd";     
        public static final String USERDATA_BALANCE = "balance";     
    
    
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
    


    public static final String LOGIN_SCENE = "primary";
}
