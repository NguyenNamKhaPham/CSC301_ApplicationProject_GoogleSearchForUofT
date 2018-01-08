package main;
import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TagFiles extends HttpServlet {
	
	//get the path for uploaded files
	private String filePath; 

    public void init( )
    {
    	filePath = getServletContext().getInitParameter("file-upload");
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

            String[] path = request.getParameterValues("tagPath");
            //if something was checked
            if(path != null){
              for(int j = 0 ; j < path.length ; j ++)
                tagFile(path[j], userId);
              	return;
            }
    }
    
    public void tagFile(String path, int userId) throws IOException
    {
    	List<String> oldFiles = TheDatabase.checkTaggedFileUser(userId,filePath);
    	path = path.replace("\\", "/");
    	//uncomment for windows users
    	//path = "/" + path;
    	if (!oldFiles.contains(path)){
    		TheDatabase.insertTaggedFile(path, userId, filePath);
    	}
    }
}
