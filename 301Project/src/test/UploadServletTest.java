package test;

import main.UploadServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.*;


public class UploadServletTest {
	
	private WebDriver driver;
	
	@Mock
	HttpServletRequest request;
	HttpServletResponse response;
	RequestDispatcher reqDis;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

	}
	
	@Test
	public void testDoPostOne() throws IOException, ServletException
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
		RequestDispatcher reqDis = mock(RequestDispatcher.class);
	
		Cookie ck = new Cookie("userId", "24");
	
		when(request.getCookies()).thenReturn(new Cookie[]{ck});
		
		when(request.getRequestDispatcher("File.html")).thenReturn(reqDis);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(responce.getWriter()).thenReturn(pw);
		
		new UploadServlet().doPost(request, responce);
		
		//verify(reqDis).include(request, responce);
		String result = sw.getBuffer().toString().trim();
		
		assertEquals("No file uploaded<br>",result);
	}

/*
	@Test
	//test with correct username and password
	public void testUI()
	{
		driver = new HtmlUnitDriver();
		driver.get("http://localhost:8080/301Project/File.html");
		
		WebElement upload = driver.findElement(By.name("file"));
		upload.sendKeys("/home/kosar/Desktop/Sys1.pdf");
		//upload.submit();
		
		driver.findElement(By.id("submit")).click();
		driver.get("http://localhost:8080/301Project/File.html");
		String result = driver.findElement(By.name("res")).getText();

		assertEquals(result,"Uploaded Filename: Sys1.pdf");
	}*/	
	

	/*
	@Test
	public void testCreatePreview() throws IOException
	{
		String cont = "hello\nthis is a test\nyup";
		File file = mock(File.class);
		BufferedReader br = mock(BufferedReader.class); 

		when(file.getName()).thenReturn("test.txt");
		when(new BufferedReader(new FileReader(file.getAbsolutePath()))).thenReturn(br);

		
		new UploadServlet().createPreview(file);
		
		assertEquals(cont,content);
	}*/
	
}
