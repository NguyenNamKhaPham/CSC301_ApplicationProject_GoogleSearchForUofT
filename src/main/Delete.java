package main;

import java.io.*;
import java.util.*;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

public class Delete extends HttpServlet {
	
	//change this only for test classes
	private String origPath="/home/kosar/Desktop/proj/301grouprepository2/301Project/";
	private String filePath = "/home/kosar/Desktop/proj/301grouprepository2/301Project/uploadFile/"; 

    public void init( )
    {
    	//get the path for uploaded files
    	origPath = getServletContext().getInitParameter("file-upload");
    	filePath = origPath +"uploadFile/";
    }
    
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }
    
    //when a post request is coming from a form (File.html) 
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

  	  Cookie ck[] = request.getCookies();
  	  int userId;
  	  if(ck != null)
  	  {
  		  userId = Integer.parseInt(ck[0].getValue());
  		  System.out.println(ck[0].getValue());
  	  }
  	  else
  	  {
  		  userId = -10;
  		  System.out.println("it didnt get the cookie");
  	  }
        //get the checked files
        String [] files = request.getParameterValues("check[]");

        //if something was checked
        if(files != null){
          for(int j = 0 ; j < files.length ; j ++)
            delete(files[j], userId);
        }
        
        //preparing the response page
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter( );
        
        //get the files in the dir
        File folder = new File (filePath);
        File [] listOfFiles = folder.listFiles();
        String fileName = "";
        String fullPath = "";
        
        //preparing the files in the dir for front end
        for (int i = 0 ; i < listOfFiles.length ; i++)
        {
        	if(listOfFiles[i].isFile())
        	{
        		//put file names in the web page
        		fileName = listOfFiles[i].getName();
        		fullPath = filePath + fileName.substring(fileName.lastIndexOf("\\")+1);
        		//shows only user's files
        		if(TheDatabase.checkFileUser(fullPath, userId, origPath))
        			out.println("<input type=\"checkbox\" class=\"test\" value=\""+fileName+"\">" + fileName + "<br>");
        	}
        }    

    }
    
    public void delete(String name, int userId) throws IOException
    {
    	IndexFiles.deleteFromIndex(name);
    	String fullPath = filePath + name.substring(name.lastIndexOf("\\")+1);
    	TheDatabase.deleteFile(fullPath, userId, origPath);
        File f = new File(fullPath);
        f.delete();
    }

}