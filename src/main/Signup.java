package main;

import java.io.*;
import java.util.*;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;


public class Signup extends HttpServlet{

	
	private String origPath;
    public void init( )
    {
    	origPath = getServletContext().getInitParameter("file-upload");
    }
    //ignore any get requests
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

    }
    
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
        String username = request.getParameter("username");
        String pass1 = request.getParameter("pass1");
        String pass2 = request.getParameter("pass2");
        Integer is = Integer.parseInt(request.getParameter("radioName"));
        //preparing the response page
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter( );
    	
        if(pass1.equals(pass2) && pass1 != null && username !=null){
        	 
        	//write account to db
        	TheDatabase.insert(username, pass1, is, origPath);
        	response.sendRedirect("LoginPage.html");
        }else{
        	
        	request.getRequestDispatcher("SignupPage.html").include(request, response);
        	out.println("<div class='frame' style='color: red'>passwords dont match </div>");
        	
        }
    }
	
	

}
