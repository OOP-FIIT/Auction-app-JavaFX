package auction.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import auction.shared.Const;

public class SQL {
   private static Connection conn;

   // INIT--------------------------------------

   public static void initSql() throws SQLException {
      // Open a connection
      conn = DriverManager.getConnection(Const.SQL.DB_URL, Const.SQL.DB_LOGIN, Const.SQL.DB_PASS);
      CreateDataBase(); //
      selectDataBase(); // Auction
      CreateTables();   // Users | Lots | Bids

   }

   private static void selectDataBase() throws SQLException{
      String sql = "USE " + Const.SQL.DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(sql);
   }

   public static boolean IsLoginExists(String login) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + Const.SQL.USERDATA + 
                     " WHERE "         + Const.SQL.USERDATA_LOGIN + " = \"" + login + "\";";
      
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
         rs.getString(Const.SQL.USERDATA_LOGIN);
         return true;
      } else
         return false;
   }

   public static int IsPaaswordCorrect(String login, String password) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + Const.SQL.USERDATA + 
                     " WHERE "         + Const.SQL.USERDATA_LOGIN    + "= \"" + login    + "\"" + 
                     " AND "           + Const.SQL.USERDATA_PASSWORD + "= \"" + password + "\"" + ";";
                     
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         return Integer.parseInt(rs.getString(Const.SQL.USERDATA_ID));
      } else
         return 0;
   }

   //CREATE-------------------------------------   

   private static void CreateDataBase() throws SQLException{
      String create = "CREATE DATABASE IF NOT EXISTS " + Const.SQL.DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }
   
   private static void CreateTables() throws SQLException{
      CreateTable_Users();
      CreateTable_Lots();
      CreateTable_Bids();
   }

   private static void CreateTable_Lots() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + Const.SQL.LOTS + " (" +
                                    Const.SQL.LOTS_ID          + " INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +
                                    Const.SQL.LOTS_SELLER_ID   + " INTEGER ," +             //Who
                                    Const.SQL.LOTS_NAME        + " VARCHAR(50), " +         //What
                                    Const.SQL.LOTS_DATE        + " DATETIME," +             //When
                                    Const.SQL.LOTS_DESCRIPTION + " VARCHAR(10000)," +       //Description
                                    " PRIMARY KEY ("+ Const.SQL.LOTS_ID +"))";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void CreateTable_Users() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + Const.SQL.USERDATA + "(" + 
                                    Const.SQL.USERDATA_ID       + " INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +  
                                    Const.SQL.USERDATA_LOGIN    + " VARCHAR(50), " +
                                    Const.SQL.USERDATA_PASSWORD + " VARCHAR(50), " +
                                    Const.SQL.USERDATA_EMAIL    + " VARCHAR(50), " +
                                    Const.SQL.USERDATA_MODE     + " VARCHAR(10), " +
                                    Const.SQL.USERDATA_BALANCE  + " INTEGER," +
                                    " PRIMARY KEY (" + Const.SQL.USERDATA_ID + "));";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void CreateTable_Bids() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + Const.SQL.BIDS + " (" + 
                                    Const.SQL.BIDS_ID       + " INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +
                                    Const.SQL.BIDS_BUYER_ID + " INTEGER,"  +      //Who
                                    Const.SQL.BIDS_DATE     + " DATETIME," +      //When
                                    Const.SQL.BIDS_LOT_ID   + " INTEGER,"  +      //Where
                                    Const.SQL.BIDS_BID      + " INTEGER);";       //How much
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   //INSERT------------------------------------

   public static int InsertUser(String login, String password, String email, String mode, int balance) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + Const.SQL.USERDATA + " (" + 
                                       Const.SQL.USERDATA_LOGIN + ", " + 
                                       Const.SQL.USERDATA_PASSWORD + ", " + 
                                       Const.SQL.USERDATA_MODE + ", " + 
                                       Const.SQL.USERDATA_BALANCE + ") " + 
                                       "VALUES('" + 
                                                login    + "' , '" + 
                                                password + "' , '" + 
                                                email    + "', '"  + 
                                                mode     + "', "   + 
                                                balance  + 
                                                ");";
      
      stmt.executeUpdate(sql);

      
      sql = "SELECT id FROM " + Const.SQL.USERDATA + "WHERE " + Const.SQL.USERDATA_LOGIN + "='" + login +"';";
      ResultSet result = stmt.executeQuery(sql);
      result.next();
      return Integer.parseInt(result.getString(Const.SQL.USERDATA_ID));
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
      String sql = "INSERT INTO " + Const.SQL.LOTS + " (" + 
                                       Const.SQL.LOTS_NAME + ", " +
                                       Const.SQL.LOTS_DATE + ", " +
                                       Const.SQL.LOTS_DESCRIPTION  + ", " + 
                                       Const.SQL.LOTS_SELLER_ID + ") " +
                                       "VALUES('" + 
                                                name + "' , '" + 
                                                date + "' , '" + 
                                                description + "' , " + 
                                                seller_id + ");";
      
      stmt.executeUpdate(sql);
   }

   public static void InsertBid(int buyer_id, String date, int lot_id, int bid) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql =   " INSERT INTO " + Const.SQL.BIDS + " ("+ 
                                          Const.SQL.BIDS_BUYER_ID + ", " + 
                                          Const.SQL.BIDS_DATE + ", " +
                                          Const.SQL.BIDS_LOT_ID + ", " +  
                                          Const.SQL.BIDS_BID + ") " + 
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
      "UPDATE " + Const.SQL.USERDATA + " " + 
      "\nSET"
      + (password != null? " " + Const.SQL.USERDATA_PASSWORD + "='"  + password  +"'," :"")
      + (login != null?    " " + Const.SQL.USERDATA_LOGIN    + "='"  + login  +"'," :"")
      + (email != null?    " " + Const.SQL.USERDATA_EMAIL    + "='"  + email  +"'," :"")
      + (mode != null?     " " + Const.SQL.USERDATA_MODE     + "='"  + mode  +"'," :"")
      + (balance != null?  " " + Const.SQL.USERDATA_BALANCE  + "="   + (balanceAdd? Const.SQL.USERDATA_BALANCE +"+":"") + balance + " " :"")
      +"\nWHERE "              + Const.SQL.USERDATA_ID       + "="   + id + ";";
      
      stmt.executeUpdate(sql);
   }

   public static void UPDATE_UserLicense(String licenseKey, int id) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = 
      "UPDATE " + Const.SQL.USERDATA + " " + 
      "\nSET "
      + Const.SQL.USERDATA_LICENSE + "='"  + licenseKey  +"', " 
      + Const.SQL.USERDATA_MODE + "='" + Const.SQL.USER_MODE_PRO + "' " 
      +"\nWHERE "              + Const.SQL.USERDATA_ID        + "="   + id + ";";
      
      stmt.executeUpdate(sql);
   }

   //SELECT------------------------------------

   public static ResultSet SELECT_Lots() throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + Const.SQL.LOTS;

      return stmt.executeQuery(sql);
   }

   public static ResultSet SELECT_UserData(int userId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = " SELECT * FROM " + Const.SQL.USERDATA + 
                   " WHERE "         + Const.SQL.USERDATA_ID + "=" + userId + ";";

      return stmt.executeQuery(sql);
   }

   public static ResultSet SELECT_Bids(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = " SELECT * FROM " + Const.SQL.BIDS + 
                   " WHERE "         + Const.SQL.BIDS_LOT_ID + "=" + lotId +
                   " ORDER BY "      + Const.SQL.BIDS_BID    + " DESC;";
               
      ResultSet res =  stmt.executeQuery(sql);

      return res;
   }

   public static int SELECT_SellerIdByLotId(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + Const.SQL.LOTS +
                     " WHERE "         + Const.SQL.LOTS_ID + "=" + lotId + ";";

      ResultSet res = stmt.executeQuery(sql);
      res.next();

      return res.getInt(Const.SQL.LOTS_SELLER_ID);
   }

   //DELELE------------------------------------

   public static void DELETE_Lot(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = " DELETE FROM " + Const.SQL.LOTS + 
                   " WHERE "       + Const.SQL.LOTS_ID + "=" + lotId + ";";

      stmt.executeUpdate(sql);
   } 

   public static void DELETE_Bids(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = " DELETE FROM " + Const.SQL.BIDS + 
                   " WHERE "       + Const.SQL.BIDS_LOT_ID + "=" + lotId + ";";

      stmt.executeUpdate(sql);
   }

}