package auction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sql_manager {
   private static final String USER = "root";
   private static final String PASS = "misha";
   private static final String DB_NAME = "USERS";
   private static final String TABLE_NAME = "USERDATA";
   private static final String DB_URL = "jdbc:mysql://localhost/";
   private static Connection conn;

   public static void init_SQL() throws SQLException {
      // Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected");
      create_db();
      select_db();
      create_tables();

   }

   private static void create_db() throws SQLException{
      String create = "CREATE DATABASE IF NOT EXISTS " + DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   private static void select_db() throws SQLException{
      String create = "USE " + DB_NAME; 
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   public static void create_tables() throws SQLException {
      String create = "CREATE TABLE " + " IF NOT EXISTS " + TABLE_NAME +
            "( login VARCHAR(50), " +
            " password VARCHAR(50), " +
            " email VARCHAR(50), " +
            " modd VARCHAR(10), " +
            " balance INTEGER)";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   public static void insert_new_user_sql(String login, String password, String email, String mode, int balance) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO " + TABLE_NAME + " (login,password,email,modd,balance) VALUES('" + login + "' , '" + password + "' , '" + email + "', '" + mode + "', " + balance  + ");";
      stmt.executeUpdate(sql);

   }

   public static boolean login_exists(String login) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + TABLE_NAME + " WHERE login = \"" + login + "\";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         rs.getString("login");
         return true;
      } else
         return false;
   }

   public static boolean password_correct(String login, String password) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM " + TABLE_NAME + " WHERE login = \"" + login + "\" AND password = " + "\"" + password
            + "\"" + ";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         return true;
      } else
         return false;
   }

}