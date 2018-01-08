package test;

import main.IndexFiles;
import main.Search;
import main.TheDatabase;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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


public class SearchTest {
	
	private WebDriver driver;
	private String path;
	@Mock
	HttpServletRequest request;
	HttpServletResponse response;
	RequestDispatcher reqDis;
	
	@Before
	public void setUp() throws IOException 
	{
		MockitoAnnotations.initMocks(this);
		path = "/home/kosar/Desktop/proj/301grouprepository2/301Project/uploadFile/";
		File f = new File(path+"testing.txt");
		boolean yes = f.createNewFile();
		FileWriter fw = new FileWriter(f.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("test");
		bw.close();
		String [] temp = {};
		IndexFiles.main(temp);
		//del = new Delete();
		//del.init();
	
	}
	
	@Test
	public void testDoPostOne() throws IOException, ServletException, SQLException
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
		//RequestDispatcher reqDis = mock(RequestDispatcher.class);

		when(request.getParameter("searchQuery")).thenReturn("test");		
		
		//when(request.getRequestDispatcher("File.html")).thenReturn(reqDis);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(responce.getWriter()).thenReturn(pw);
		
		new Search().doPost(request, responce);
		
		//verify(reqDis).include(request, responce);
		String result = sw.getBuffer().toString().trim().substring(0,20);
		
		System.out.println( result);
		
		assertEquals("<br><a href=Download",result);
	}
		
	
}

