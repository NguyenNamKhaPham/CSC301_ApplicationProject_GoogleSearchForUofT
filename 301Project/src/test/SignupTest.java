package test;

import main.Signup;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.*;


public class SignupTest {
	
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
	//test with not matching passwords
	public void testDoPostOne() throws IOException, ServletException
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
		RequestDispatcher reqDis = mock(RequestDispatcher.class);
	
		
		//username and 2 not matching passes
		when(request.getParameter("username")).thenReturn("userTest");
		when(request.getParameter("pass1")).thenReturn("passTest1");
		when(request.getParameter("pass2")).thenReturn("passTest2");
		when(request.getParameter("radioName")).thenReturn("1");
		
		when(request.getRequestDispatcher("SignupPage.html")).thenReturn(reqDis);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(responce.getWriter()).thenReturn(pw);
		
		new Signup().doPost(request, responce);
		
		verify(reqDis).include(request, responce);
		String result = sw.getBuffer().toString().trim();
		
		System.out.println("result: " + result);
		
		assertEquals("passwords dont match <br>",result);
	}
	
	@Test
	//test with matching passwords
	public void testDoPostTwo() throws IOException, ServletException
	{
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
	
		
		//matching pass
		when(request.getParameter("username")).thenReturn("test");
		when(request.getParameter("pass1")).thenReturn("test");
		when(request.getParameter("pass2")).thenReturn("test");
		when(request.getParameter("radioName")).thenReturn("1");
		
		
		new Signup().doPost(request, responce);
		
		verify(responce).sendRedirect("LoginPage.html");
		
		
	}
	
	@Test
	//test with correct username and password
	public void testUI()
	{
		driver = new HtmlUnitDriver();
		driver.get("http://localhost:8080/301Project/SignupPage.html");
		String title = driver.getTitle();
		assertEquals("Group 2 signup page",title);
		
		enterTwoPass("wrong", "wr");

		assertEquals(title,"Group 2 signup page");
	}	
	
	private void enterTwoPass(String pass1, String pass2)
	{
		WebElement el1 = driver.findElement(By.name("username"));
		WebElement el2 = driver.findElement(By.name("pass1"));
		WebElement el3 = driver.findElement(By.name("pass2"));
		
		el1.sendKeys("whatever");
		el2.sendKeys(pass1);
		el3.sendKeys(pass2);
		el1.submit();
	}
}
