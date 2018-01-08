package main;

import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TagServlet extends HttpServlet {
	
	//get the path for uploaded files
	private String filePath; 
    private String origPath;
    public void init( )
    {
    	origPath = getServletContext().getInitParameter("file-upload");
    	filePath = origPath +"uploadFile/";
    }

    public void doGet(HttpServletRequest request,HttpServletResponse response)
        throws ServletException, java.io.IOException {

}

    public void doPost(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, java.io.IOException {
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
      		  return;
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
            
            
            List<String> path = TheDatabase.checkTaggedFileUser(userId, origPath);
            System.out.println("Got path " + path);
            //preparing the files in the dir for front end
            for (int i = 0 ; i < path.size() ; i++)
            {
            	String[] tempPath = path.get(i).split("/");
            	fileName = tempPath[tempPath.length-1];
            	out.println("<input type=\"checkbox\" class=\"test2\" value=\""+fileName+"\">" + fileName + "<br>");
            }
    }
    
    public void delete(String name, int userId) throws IOException
    {
    	String fullPath = filePath + name.substring(name.lastIndexOf("\\")+1);
    	TheDatabase.unTaggedFile(fullPath, userId, origPath);
    }
}
