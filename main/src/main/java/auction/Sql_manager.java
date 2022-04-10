package auction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sql_manager {
   static final String DB_URL = "jdbc:mysql://localhost/USERS";
   static final String USER = "root";
   static final String PASS = "misha";
   static final String DB_NAME = "USERS";

   static Connection conn;

   public static void start() throws SQLException {
      // Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected");
      // create_tables();
      // insert_sql("mishaaa");
   }

   public static void create_tables() throws SQLException {
      String create = "CREATE TABLE login_password " +
            "(id INTEGER not NULL, " +
            " login VARCHAR(20), " +
            " password VARCHAR(20))";
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(create);
   }

   public static void insert_sql(String login, String password) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "INSERT INTO login_password (id,login,password) VALUES(1,'user', 'pass')";
      stmt.executeUpdate(sql);

   }

   public static boolean login_exists(String login) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM login_password WHERE login = \"" + login + "\";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         rs.getString("login");
         return true;
      } else
         return false;
   }

   public static boolean password_correct(String login, String password) throws SQLException {
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM login_password WHERE login = \"" + login + "\" AND password = " + "\"" + password
            + "\"" + ";";
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
         return true;
      } else
         return false;
   }

}