package test;

import main.Login;
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


public class LoginTest {
	
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
	//test with wrong username and password
	public void testDoPostOne() throws IOException, ServletException
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
		RequestDispatcher reqDis = mock(RequestDispatcher.class);
	
		
		//wrong username and pass
		when(request.getParameter("username")).thenReturn("userTest");
		when(request.getParameter("pass")).thenReturn("passTest");
		when(request.getParameter("loginType")).thenReturn("0");
		
		when(request.getRequestDispatcher("LoginPage.html")).thenReturn(reqDis);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(responce.getWriter()).thenReturn(pw);
		
		new Login().doPost(request, responce);
		
		verify(reqDis).include(request, responce);
		String result = sw.getBuffer().toString().trim();
		
		System.out.println("result: " + result);
		
		assertEquals("wrong username or password or wrong role <br>",result);
	}
	
	@Test
	//test with correct username and password
	public void testDoPostTwo() throws IOException, ServletException
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse responce = mock(HttpServletResponse.class);
		RequestDispatcher reqDis = mock(RequestDispatcher.class);
	
		
		//wrong username and pass
		when(request.getParameter("username")).thenReturn("kosar");
		when(request.getParameter("pass")).thenReturn("kosar");
		when(request.getParameter("loginType")).thenReturn("1");
		
		when(request.getRequestDispatcher("Search.html")).thenReturn(reqDis);
		
		
		new Login().doPost(request, responce);
		
		verify(reqDis).include(request, responce);
		
		
	}
	
	@Test
	//test with correct username and password
	public void testUI()
	{
		driver = new HtmlUnitDriver();
		driver.get("http://localhost:8080/301Project/LoginPage.html");
		String title = driver.getTitle();
		assertEquals("Group2_Search Login",title);
		
		enterUserPass("wrong", "wr");

		assertEquals(title,"Group2_Search Login");
	}	
	
	private void enterUserPass(String user, String pass)
	{
		WebElement el1 = driver.findElement(By.name("username"));
		WebElement el2 = driver.findElement(By.name("pass"));
		
		el1.sendKeys(user);
		el2.sendKeys(pass);
		el1.submit();
	}
}
