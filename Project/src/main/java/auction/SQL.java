package auction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
   private static final String USER = "root";
   private static final String PASS = "misha";
   private static final String DB_NAME = "AUCTION";
   private static final String BIDS_TABLENAME = "Biiid";    //Bids  (now used test names like AAAA)
   private static final String USERS_TABLENAME = "USERDATA";     //Lots
   private static final String LOTS_TABLENAME = "lll";
   private static final String DB_URL = "jdbc:mysql://localhost/";
   private static Connection conn;

   // INIT--------------------------------------

   public static void InitSql() throws SQLException {
      // Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      CreateDataBase(); //
      SelectDataBase(); // Auction
      CreateTables();   // Users | Lots | Bids

   }

   private static void SelectDataBase() throws SQLException{
      String create = "USE " + DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   public static boolean IsLoginExists(String login) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + USERS_TABLENAME + " WHERE login = \"" + login + "\";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         rs.getString("login");
         return true;
      } else
         return false;
   }

   public static int IsPaaswordCorrect(String login, String password) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + USERS_TABLENAME + " WHERE login = \"" + login + "\" AND password = " + "\"" + password
            + "\"" + ";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         return Integer.parseInt(rs.getString("id"));
      } else
         return 0;
   }

   //CREATE-------------------------------------   

   private static void CreateDataBase() throws SQLException{
      String create = "CREATE DATABASE IF NOT EXISTS " + DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }
   
   private static void CreateTables() throws SQLException{
      CreateTable_Users();
      CreateTable_Lots();
      CreateTable_Bids();
   }

   private static void CreateTable_Lots() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + LOTS_TABLENAME +
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
      String create = "CREATE TABLE " + " IF NOT EXISTS " + USERS_TABLENAME +
            "(id INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +  
            "login VARCHAR(50), " +
            " password VARCHAR(50), " +
            " email VARCHAR(50), " +
            " modd VARCHAR(10), " +
            " balance INTEGER," +
            " PRIMARY KEY (id))";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void CreateTable_Bids() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + BIDS_TABLENAME +
            " (id INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +
            " buyer_id INTEGER," +  //Who
            " date DATETIME," +     //When
            " lot_id INTEGER," +    //Where
            " bid INTEGER);";       //How much
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   //INSERT------------------------------------

   public static int InsertUser(String login, String password, String email, String mode, int balance) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + USERS_TABLENAME + " (login,password,email,modd,balance) VALUES('" + login + "' , '" + password + "' , '" + email + "', '" + mode + "', " + balance  + ");";
      stmt.executeUpdate(sql);

      
      sql = "SELECT id FROM " + USERS_TABLENAME + "WHERE login='" + login +"';";
      ResultSet result = stmt.executeQuery(sql);
      result.next();
      return Integer.parseInt(result.getString("id"));
   }

   /**
    * Inserts new Lot in DataBase-Lots_table
    * @param name String
    * @param date "1000-01-01 00:00:00"
    * @param description
    * @throws SQLException
    */
   public static void InsertLot(String name, String description, String date, int sellerId) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + LOTS_TABLENAME + " (name, date, description, seller_id) VALUES('" + name + "' , '" + date + "' , '" + description + "' , " + sellerId + ");";
      stmt.executeUpdate(sql);
   }

   public static void InsertBid(int Buyer, String Date, int Lot, int bid) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql =   " INSERT INTO " + BIDS_TABLENAME + 
                     " (buyer_id, date, lot_id, bid) " + 
                     " VALUES('" + Buyer + "' , '" 
                                 + Date + "' , "  
                                 + Lot + ", " 
                                 + bid + ");";
      stmt.executeUpdate(sql);
   }

   //UPDATE------------------------------------

   public static void UPDATE_User(String password, String login, String email, String mode, Integer balance, boolean balanceAdd, int id) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = "UPDATE " + USERS_TABLENAME + " \nSET"
      + (password != null?  " password='"  + password  +"'," :"")
      + (login != null?     " login='"     + login     +"'," :"")
      + (email != null?     " email='"     + email     +"'," :"" )
      + (mode != null?      " mode='"      + mode      +"'," :"" )
      + (balance != null?   " balance="  + (balanceAdd?"balance+":"")  + balance   +"" :"" )
      +" \nWHERE id=" + id + ";";
      
      stmt.executeUpdate(sql);
   }

   //SELECT------------------------------------

   public static ResultSet SELECT_Lots() throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + LOTS_TABLENAME;
      return stmt.executeQuery(sql);
   }

   public static ResultSet SELECT_UserData(int userId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + USERS_TABLENAME + " WHERE id=" + userId + ";";
      return stmt.executeQuery(sql);
   }

   public static ResultSet SELECT_Bids(int lotId) throws SQLException{
      int winner = -1;
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + BIDS_TABLENAME + 
                     " WHERE lot_id=" + lotId +
                     " ORDER BY bid DESC;"
                     ;
               
      ResultSet res =  stmt.executeQuery(sql);

      return res;
   }

   public static int SELECT_SellerIdByLotId(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql =   " SELECT * FROM " + LOTS_TABLENAME +
                     " WHERE id=" + lotId + ";";
      ResultSet res = stmt.executeQuery(sql);

      res.next();
      return res.getInt("seller_id");
   }

   //DELELE------------------------------------

   public static void DELETE_Lot(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql =   "DELETE FROM " + LOTS_TABLENAME + 
                     " WHERE id=" + lotId + ";";
      stmt.executeUpdate(sql);
   } 

   public static void DELETE_Bids(int lotId) throws SQLException{
      Statement stmt = conn.createStatement();
      String sql =   "DELETE FROM " + BIDS_TABLENAME + 
                     " WHERE lot_id=" + lotId + ";";
      stmt.executeUpdate(sql);
   }

}