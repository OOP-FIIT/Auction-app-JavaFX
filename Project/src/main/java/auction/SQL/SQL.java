package auction.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import auction.shared.Const;

public class SQL {
   private static Connection conn;

   // INIT--------------------------------------

   public static void InitSql() throws SQLException {
      // Open a connection
      conn = DriverManager.getConnection(Const.DB_URL, Const.DB_LOGIN, Const.DB_PASS);
      CreateDataBase(); //
      SelectDataBase(); // Auction
      CreateTables();   // Users | Lots | Bids

   }

   private static void SelectDataBase() throws SQLException{
      String sql = "USE " + Const.DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(sql);
   }

   public static boolean IsLoginExists(String login) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + Const.USERDATA + 
                     " WHERE "         + Const.USERDATA_LOGIN + " = \"" + login + "\";";
      
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
         rs.getString(Const.USERDATA_LOGIN);
         return true;
      } else
         return false;
   }

   public static int IsPaaswordCorrect(String login, String password) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + Const.USERDATA + 
                     " WHERE "         + Const.USERDATA_LOGIN    + "= \"" + login    + "\"" + 
                     " AND "           + Const.USERDATA_PASSWORD + "= \"" + password + "\"" + ";";
                     
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         return Integer.parseInt(rs.getString(Const.USERDATA_ID));
      } else
         return 0;
   }

   //CREATE-------------------------------------   

   private static void CreateDataBase() throws SQLException{
      String create = "CREATE DATABASE IF NOT EXISTS " + Const.DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }
   
   private static void CreateTables() throws SQLException{
      CreateTable_Users();
      CreateTable_Lots();
      CreateTable_Bids();
   }

   private static void CreateTable_Lots() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + Const.LOTS +
            " (id INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +
            " seller_id INTEGER ," +         //Who
            " name VARCHAR(50), " +          //What
            " date DATETIME," +              //When
            " description VARCHAR(10000)," + //Description
            " PRIMARY KEY (id))";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void CreateTable_Users() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + Const.USERDATA + "(" + 
                                    Const.USERDATA_ID       + " INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +  
                                    Const.USERDATA_LOGIN    + " VARCHAR(50), " +
                                    Const.USERDATA_PASSWORD + " VARCHAR(50), " +
                                    Const.USERDATA_EMAIL    + " VARCHAR(50), " +
                                    Const.USERDATA_MODD     + " VARCHAR(10), " +
                                    Const.USERDATA_BALANCE  + " INTEGER," +
                                    " PRIMARY KEY (" + Const.USERDATA_ID + "));";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void CreateTable_Bids() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + Const.BIDS + " (" + 
                                    Const.BIDS_ID       + " INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +
                                    Const.BIDS_BUYER_ID + " INTEGER,"  +      //Who
                                    Const.BIDS_DATE     + " DATETIME," +      //When
                                    Const.BIDS_LOT_ID   + " INTEGER,"  +      //Where
                                    Const.BIDS_BID      + " INTEGER);";       //How much
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   //INSERT------------------------------------

   public static int InsertUser(String login, String password, String email, String mode, int balance) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + Const.USERDATA + " (" + 
                                       Const.USERDATA_LOGIN + ", " + 
                                       Const.USERDATA_PASSWORD + ", " + 
                                       Const.USERDATA_MODD + ", " + 
                                       Const.USERDATA_BALANCE + ") " + 
                                       "VALUES('" + 
                                                login + "' , '" + 
                                                password + "' , '" + 
                                                email + "', '" + 
                                                mode + "', " + 
                                                balance  + ");";
      
      stmt.executeUpdate(sql);

      
      sql = "SELECT id FROM " + Const.USERDATA + "WHERE " + Const.USERDATA_LOGIN + "='" + login +"';";
      ResultSet result = stmt.executeQuery(sql);
      result.next();
      return Integer.parseInt(result.getString(Const.USERDATA_ID));
   }

   /**
    * Inserts new Lot in DataBase-Lots_table
    * @param name String
    * @param date "1000-01-01 00:00:00"
    * @param description
    * @throws SQLException
    */
   public static void InsertLot(String name, String description, String date, int seller_id) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + Const.LOTS + " (" + 
                                       Const.LOTS_NAME + ", " +
                                       Const.LOTS_DATE + ", " +
                                       Const.LOTS_DESCRIPTION  + ", " + 
                                       Const.LOTS_SELLER_ID + ") " +
                                       "VALUES('" + 
                                                name + "' , '" + 
                                                date + "' , '" + 
                                                description + "' , " + 
                                                seller_id + ");";
      
      stmt.executeUpdate(sql);
   }

   public static void InsertBid(int buyer_id, String date, int lot_id, int bid) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql =   " INSERT INTO " + Const.BIDS + " ("+ 
                                          Const.BIDS_BUYER_ID + ", " + 
                                          Const.BIDS_DATE + ", " +
                                          Const.BIDS_LOT_ID + ", " +  
                                          Const.BIDS_BID + ") " + 
                                          " VALUES('" + buyer_id + "' , '" 
                                                      + date + "' , "  
                                                      + lot_id + ", " 
                                                      + bid + ");";

      stmt.executeUpdate(sql);
   }

   //UPDATE------------------------------------

   public static void UPDATE_User(String password, String login, String email, String mode, Integer balance, boolean balanceAdd, int id) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = 
      "UPDATE " + Const.USERDATA + " " + 
      "\nSET"
      + (password != null? " " + Const.USERDATA_PASSWORD + "='"  + password  +"'," :"")
      + (login != null?    " " + Const.USERDATA_LOGIN    + "='"  + login  +"'," :"")
      + (email != null?    " " + Const.USERDATA_EMAIL    + "='"  + email  +"'," :"")
      + (mode != null?     " " + Const.USERDATA_MODD     + "='"  + mode  +"'," :"")
      + (balance != null?  " " + Const.USERDATA_BALANCE  + "="   + (balanceAdd? Const.USERDATA_BALANCE +"+":"") + balance + " " :"")
      +"\nWHERE "              + Const.USERDATA_ID       + "="   + id + ";";
      
      stmt.executeUpdate(sql);
   }

   //SELECT------------------------------------

   public static ResultSet SELECT_Lots() throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + Const.LOTS;

      return stmt.executeQuery(sql);
   }

   public static ResultSet SELECT_UserData(int userId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = " SELECT * FROM " + Const.USERDATA + 
                   " WHERE "         + Const.USERDATA_ID + "=" + userId + ";";

      return stmt.executeQuery(sql);
   }

   public static ResultSet SELECT_Bids(int lotId) throws SQLException{
      int winner = -1;
      Statement stmt = conn.createStatement();
      String sql = " SELECT * FROM " + Const.BIDS + 
                   " WHERE "         + Const.BIDS_LOT_ID + "=" + lotId +
                   " ORDER BY "      + Const.BIDS_BID    + " DESC;";
               
      ResultSet res =  stmt.executeQuery(sql);

      return res;
   }

   public static int SELECT_SellerIdByLotId(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + Const.LOTS +
                     " WHERE "         + Const.LOTS_ID + "=" + lotId + ";";

      ResultSet res = stmt.executeQuery(sql);
      res.next();

      return res.getInt(Const.LOTS_SELLER_ID);
   }

   //DELELE------------------------------------

   public static void DELETE_Lot(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = " DELETE FROM " + Const.LOTS + 
                   " WHERE "       + Const.LOTS_ID + "=" + lotId + ";";

      stmt.executeUpdate(sql);
   } 

   public static void DELETE_Bids(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = " DELETE FROM " + Const.BIDS + 
                   " WHERE "       + Const.BIDS_LOT_ID + "=" + lotId + ";";

      stmt.executeUpdate(sql);
   }

}