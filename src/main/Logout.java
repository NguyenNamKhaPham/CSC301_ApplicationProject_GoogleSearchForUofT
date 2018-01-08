package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logout extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//clear any active cookies
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		    for (int i = 0; i < cookies.length; i++) {
		        cookies[i].setValue("");
		        cookies[i].setPath("/");
		        cookies[i].setMaxAge(0);
		        response.addCookie(cookies[i]);
		    }
		//redirect to login
		response.sendRedirect("LoginPage.html");
		
	}
	
}
