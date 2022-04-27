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

   public static void InitSql() throws SQLException {
      // Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      CreateDataBase(); //
      SelectDataBase(); // Auction
      CreateTables();   // Users | Lots | Bids

   }

   private static void CreateDataBase() throws SQLException{
      String create = "CREATE DATABASE IF NOT EXISTS " + DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void SelectDataBase() throws SQLException{
      String create = "USE " + DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void CreateTables() throws SQLException{
      CreateTable_Users();
      CreateTable_Lots();
      CreateTable_Bids();
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

   public static boolean IsPaaswordCorrect(String login, String password) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + USERS_TABLENAME + " WHERE login = \"" + login + "\" AND password = " + "\"" + password
            + "\"" + ";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         return true;
      } else
         return false;
   }

   public static void CreateTable_Lots() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + LOTS_TABLENAME +
            " (id INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +
            " seller INTEGER ," +            //Who
            " name VARCHAR(50), " +          //What
            " date DATETIME," +              //When
            " description VARCHAR(10000)," + //Description
            " PRIMARY KEY (id))";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   public static void CreateTable_Users() throws SQLException {
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

   public static void CreateTable_Bids() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + BIDS_TABLENAME +
            " (id INTEGER NOT NULL UNIQUE AUTO_INCREMENT," +
            " buyer_id INTEGER," +  //Who
            " date DATETIME," +     //When
            " lot_id INTEGER);" ;   //Where
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   public static void InsertUser(String login, String password, String email, String mode, int balance) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + USERS_TABLENAME + " (login,password,email,modd,balance) VALUES('" + login + "' , '" + password + "' , '" + email + "', '" + mode + "', " + balance  + ");";
      stmt.executeUpdate(sql);

   }

   public static void InsertLot(String name, String date, String description) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + LOTS_TABLENAME + " (name, date, description) VALUES('" + name + "' , '" + date + "' , '" + description + "');";
      stmt.executeUpdate(sql);
   }

   public static void InsertBid(String Buyer, String Date, String Lot) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + BIDS_TABLENAME + " (buyer, date, lot_id) VALUES('" + Buyer + "' , '" + Date + "' , '" + Lot + "');";
      stmt.executeUpdate(sql);
   }

}