package main;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Search extends HttpServlet {

    /**
	 * 
	 */
	//change this only for test classes
	private String filePath = "/home/kosar/Desktop/proj/301grouprepository2/301Project/";
	private static final long serialVersionUID = 1L;
	public void init( )
    {
    	filePath = getServletContext().getInitParameter("file-upload");

    }
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

    }
    //when a post reguest is coming from a form 
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain");
    	PrintWriter out = response.getWriter();
    	
    	String index = filePath;
    	String field = "contents";
    	String queries = request.getParameter("searchQuery");
    	boolean raw = false;
    	int hitsPerPage = 10;
    	
       try {
    	   String res= SearchFiles.search(index, field, queries, raw, hitsPerPage);
    	   out.print(res);
    	   
       } catch (Exception e) {
    	   e.printStackTrace();
       }
        
        //preparing the response page
        //response.sendRedirect("DisplaySearch.html");
        	//response.sendRedirect("UploadFile.html");
    }

}