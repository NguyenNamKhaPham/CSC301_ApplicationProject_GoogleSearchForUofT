package main;

import main.TheDatabase;
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

public class Login extends HttpServlet {
	
	//change this only for test classes
	private String origPath="/home/kosar/Desktop/proj/301grouprepository2/301Project/";
    
	public void init( )
    {
    	origPath = getServletContext().getInitParameter("file-upload");
    }
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

    }
//when a post reguest is coming from a form 
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

    	//get username and password from the page
        String username = request.getParameter("username");
        String pass = request.getParameter("pass");
        Integer is = Integer.parseInt(request.getParameter("loginType"));
        
        //preparing the response page
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter( );
        
        //check if the user exist in database
        if (TheDatabase.checkIsThere(username,pass, is,origPath) != -1)
        {
        	int userId = TheDatabase.checkIsThere(username,pass,is, origPath);
        
        	Cookie ck = new Cookie("userId", Integer.toString(userId));
        	response.addCookie(ck);
        	//out.println("<h1>Welcome, " + username + "</h1>");
        	request.getRequestDispatcher("Search.html").include(request, response);
        	//response.sendRedirect("UploadFile.html");

        } 
        else 
        {
        	request.getRequestDispatcher("LoginPage.html").include(request, response);
            out.println("<div class='frame' style='color: red;'>wrong username or password or wrong role </div>");
        }

      

    }

}