package main;

import java.sql.*;
public class MakeDatabase
{
  public static void main( String args[] )
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:/Users/teddelicious/301grouprepository2/301Project/User.db");
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "CREATE TABLE USERS " +
              "(EMAIL  CHAR(50) PRIMARY KEY NOT NULL," +
    		  " PASS   CHAR(50)             NOT NULL,"+
              " STD    INT                  NOT NULL,"+
    		  " ID     INT                  NOT NULL)"; 

      stmt.executeUpdate(sql);
      
      stmt = c.createStatement();
      sql = "CREATE TABLE FILES " +
              "(PATH  CHAR(50) PRIMARY KEY NOT NULL," +
    		  " ID    INT                  NOT NULL)"; 
      stmt.executeUpdate(sql);

      
      // Create tagged file database
      stmt = c.createStatement();
      sql = "CREATE TABLE TAGGED " +
          "(PATH  CHAR(50) NOT NULL," +
          " USERID    INT                  NOT NULL)";
      stmt.executeUpdate(sql);


      
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Table created successfully");
  }
}
