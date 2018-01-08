package test;

import main.Delete;
import main.TheDatabase;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.*;


public class DeleteTest {
	
	private WebDriver driver;
	private Delete del;
	private String path;
	@Mock
	HttpServletRequest request;
	HttpServletResponse response;
	RequestDispatcher reqDis;
	
	@Before
	public void setUp() throws IOException 
	{
		MockitoAnnotations.initMocks(this);
		path = "/home/kosar/Desktop/proj/301grouprepository2/301Project/";
		File f = new File(path+"uploadFile/test10.txt");
		boolean yes = f.createNewFile();
		//del = new Delete();
		//del.init();
	
	}
	
	@Test
	public void testDoPostOne() throws IOException, ServletException, SQLException
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
		//RequestDispatcher reqDis = mock(RequestDispatcher.class);
		Cookie ck = new Cookie("userId", "100");
		String [] files = {};
		TheDatabase.insertFile(path+"uploadFile/test10.txt", 100,path);
		
		
		when(request.getCookies()).thenReturn(new Cookie[]{ck});
		when(request.getParameterValues("check[]")).thenReturn(files);
		
		//when(request.getRequestDispatcher("File.html")).thenReturn(reqDis);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(responce.getWriter()).thenReturn(pw);
		
		new Delete().doPost(request, responce);
		
		//verify(reqDis).include(request, responce);
		String result = sw.getBuffer().toString().trim();
		
		System.out.println( result);
		
		assertEquals("<input type=\"checkbox\" class=\"test\" value=\"test10.txt\">test10.txt<br>",result);
	}
		
	@Test
	public void testDoPostTwo() throws IOException, ServletException, SQLException
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
		//RequestDispatcher reqDis = mock(RequestDispatcher.class);
		Cookie ck = new Cookie("userId", "100");
		String [] files = {"test10.txt"};
		
		when(request.getCookies()).thenReturn(new Cookie[]{ck});
		when(request.getParameterValues("check[]")).thenReturn(files);
		
		//when(request.getRequestDispatcher("File.html")).thenReturn(reqDis);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(responce.getWriter()).thenReturn(pw);
		
		new Delete().doPost(request, responce);
		
		//verify(reqDis).include(request, responce);
		String result = sw.getBuffer().toString().trim();
		
		System.out.println( result);
		
		assertEquals("",result);
	}
}
