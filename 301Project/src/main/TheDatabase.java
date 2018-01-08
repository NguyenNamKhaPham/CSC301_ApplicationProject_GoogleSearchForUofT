package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheDatabase
{
  private static Connection con; 
//insert into the the data base 
  //is var is gonna be 0 if it is instructor, other wise student as 1
  
  private static void connect(String path){
	  
	  try {
		  
		  String workingDir = System.getProperty("user.dir");
		  System.out.println(workingDir);
	  	  Class.forName("org.sqlite.JDBC");
	  	  //replace with your local path to db file
	  	  con = DriverManager.getConnection("jdbc:sqlite:"+path+"User.db");
	    } catch ( Exception e ) {
	      System.err.println("could not connect to db");
	      System.exit(0);
	    }
	  
	  
  }
  
  
  public static void insert(String email, String password, int is, String path)
  {	
	  //generating a new id for user
	  int newId = userSize(path) + 1;
	  
      connect(path);
      PreparedStatement ps;    
      String query = "INSERT INTO USERS (EMAIL, PASS, STD, ID) " +
                   "VALUES (?, ?, ?, ? );"; 
      
      try{
      ps = con.prepareStatement(query);
      ps.setString(1, email);
      ps.setString(2, password);
      ps.setInt(3, is);
      ps.setInt(4, newId);
      ps.executeUpdate();
  	  ps.close();
      con.close();

      } catch(Exception e){
    	  System.err.println("Insert failed");
	      System.exit(0);
      }
    System.out.println("inserted successfully");
  }
  //returns the user id if the user data already exists in the database and -1 if it doesn't,
  //can be used in login and signup for preventing from registering again!
  public static int checkIsThere(String email, String password, int is, String path)
  {
	  
	connect(path); 
    PreparedStatement ps;    
	String query = "SELECT ID FROM USERS WHERE EMAIL = ? AND PASS = ? and STD = ? ;";
	int result = -1;

    try {

    	ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);
        ps.setInt(3, is);

        ResultSet rs = ps.executeQuery();
      //if no row exists     
      if(!rs.isBeforeFirst()) {
      	 ps.close();
         con.close();
         rs.close();
    	 return result;
    	 }
      result = rs.getInt("ID");
  	  ps.close();
      con.close();
      
    } catch ( Exception e ) {
      e.printStackTrace();
      System.err.println("lookup failed");
      System.exit(0);
    }
    
    return result;
  }
  //return the size of the table 
  public static int userSize(String path)
  {
	  	int result = -1;
		connect(path); 
	    PreparedStatement ps;    
		String query = "SELECT COUNT(*) AS count FROM USERS;";
	    try {

	    	ps = con.prepareStatement(query);

	        ResultSet rs = ps.executeQuery();
	        result = rs.getInt("count");
	  	    ps.close();
	        con.close();
	      
	    } catch ( Exception e ) {
	      e.printStackTrace();
	      System.err.println("Count failed");
	      System.exit(0);
	    }
	    return result;
  }
  
  //insert the file path for the user who uploaded it
  public static void insertFile(String path, int id, String dbPath) throws SQLException
  {	
	 
      connect(dbPath);
      PreparedStatement ps;    
      String query = "INSERT INTO FILES (PATH, ID) " +
                     "VALUES (?, ?);"; 
      
 
      ps = con.prepareStatement(query);
      ps.setString(1, path);
      ps.setInt(2, id);
      ps.executeUpdate();
  	  ps.close();
      con.close();

      
    System.out.println("inserted successfully");
  }
  
  //check if the file is for the specific user
  public static boolean checkFileUser(String path, int id, String dbPath)
  {
	  
	connect(dbPath); 
    PreparedStatement ps;    
	String query = "SELECT * FROM FILES WHERE PATH = ? AND ID = ? ;";
	boolean result = false;

    try {

    	ps = con.prepareStatement(query);
        ps.setString(1, path);
        ps.setInt(2, id);

        ResultSet rs = ps.executeQuery();
      //if no row exists     
      if(!rs.isBeforeFirst()) {
      	 ps.close();
         con.close();
         rs.close();
    	 return result;
    	 }
      result = true;
  	  ps.close();
      con.close();
      
    } catch ( Exception e ) {
      e.printStackTrace();
      System.err.println("lookup failed");
      System.exit(0);
    }
    
    return result;
  }
  
  public static void deleteFile(String fullPath, int userId, String dbPath){
	  connect(dbPath); 
	  PreparedStatement ps;    
	  String query = "DELETE FROM FILES WHERE PATH = ? AND ID = ? ;";

	    try {   	

	    	ps = con.prepareStatement(query);
	        ps.setString(1, fullPath);
	        ps.setInt(2, userId);
	        ps.executeUpdate();
	  	  	ps.close();
	  	  	con.close();
	      
	    } catch ( Exception e ) {
	      e.printStackTrace();
	      System.err.println("delete failed");
	      System.exit(0);
	    }

  }
  
  //insert the tagged fileId to user
  public static void insertTaggedFile(String path, int userId, String dbPath)
  {
      connect(dbPath);
      PreparedStatement ps;
      String query = "INSERT INTO TAGGED (PATH, USERID) " +
            "VALUES (?, ?);";
      try{
            ps = con.prepareStatement(query);
            ps.setString(1, path);
            ps.setInt(2, userId);
            ps.executeUpdate();
            ps.close();
            con.close();
      } catch(Exception e){
            System.err.println(e);
            System.err.println(path);
            System.err.println(userId);
            System.err.println("Tagged file failed");
            System.exit(0);
      }
      System.out.println("Tagged successfully");
  }
  
  // Untagged files from user
  public static void unTaggedFile(String path, int userId, String dbPath)
  {
	  connect(dbPath); 
	  PreparedStatement ps;    
	  String query = "DELETE FROM TAGGED WHERE PATH = ? AND USERID = ? ;";
	    try {   	
	    	ps = con.prepareStatement(query);
	        ps.setString(1, path);
	        ps.setInt(2, userId);
	        ps.executeUpdate();
	  	  	ps.close();
	  	  	con.close();
	      
	    } catch ( Exception e ) {
	      e.printStackTrace();
	      System.err.println("delete failed");
	      System.exit(0);
	    }

  }
  
  //check if the file is for the specific user
  public static List<String> checkTaggedFileUser(int userId, String dbPath)
  {
		connect(dbPath); 
	    PreparedStatement ps;    
	    String query = "SELECT * FROM TAGGED WHERE USERID = ? ;";
	    ArrayList<String> path = new ArrayList<String>();
	    try {

	    	ps = con.prepareStatement(query);
	        ps.setInt(1, userId);

	        ResultSet rs = ps.executeQuery();
	        
	      //if no row exists     
	      if(!rs.isBeforeFirst()) {
	         System.out.println("no tagged files");
	      	 ps.close();
	         con.close();
	         rs.close();
	         return path;
	      }
		  while(rs.next()){
			  path.add(rs.getString("PATH"));
		  }
	      ps.close();
	      con.close();
	      rs.close();
		  return path;
	    }
	    catch ( Exception e ) {
	        e.printStackTrace();
	        System.err.println("tagged files fetch failed");
	      }
		return path;
  }
}

