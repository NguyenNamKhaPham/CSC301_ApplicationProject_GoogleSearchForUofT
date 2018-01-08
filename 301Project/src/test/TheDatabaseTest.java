package test;

import main.TheDatabase;
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
import java.sql.SQLException;

import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.*;


public class TheDatabaseTest {
	

	String email;
	String pass;
	String path;
	int is;
	int id;
	String orig;
	
	@Before
	public void setUp()
	{
		email = "test2@mail";
		pass = "justtest2";
		is = 0;
		path = "something/some";
		id = 20;
		orig = "/home/kosar/Desktop/proj/301grouprepository2/301Project/";
	}
	
	@Test
	//test insert function in thedatabase
	public void testInsert()
	{
		TheDatabase.insert(email, pass, is,orig);
		assertTrue(TheDatabase.checkIsThere(email, pass, is, orig) > -1); 
	}
		
	@Test
	//test check is there function in thedatabase
	public void testCheckIsThere()
	{
		assertTrue(TheDatabase.checkIsThere(email+"some", pass+"some", is,orig) == -1); 
	}
	
	@Test
	//test userSize function in thedatabase
	public void testUserSize()
	{
		assertEquals(TheDatabase.userSize(orig), 6); 
	}
	
	@Test
	//test insertFile function in thedatabase
	public void testInsertFile() throws SQLException
	{
		TheDatabase.insertFile(path,id,orig);
		assertTrue(TheDatabase.checkFileUser(path,id,orig)); 
	}
	
	@Test
	//test checkFileUser function in thedatabase
	public void testCheckFileUser() 
	{
		assertTrue(!TheDatabase.checkFileUser(path+"someeee",id,orig)); 
	}
	
	@Test
	//test deletetFile function in thedatabase
	public void testDeleteFile() 
	{
		TheDatabase.deleteFile(path,id,orig);
		assertTrue(!TheDatabase.checkFileUser(path,id,orig)); 
	}
	
	@Test
	//test insertTaggedFile function in thedatabase
	public void testInsertTaggedFile() throws SQLException
	{
		TheDatabase.insertTaggedFile(path,id,orig);
		assertTrue(TheDatabase.checkTaggedFileUser(id,orig) != null); 
	}
	
	@Test
	//test unTaggedFile function in thedatabase
	public void testUntaggedFile() 
	{
		TheDatabase.unTaggedFile(path,id,orig);
		assertTrue(TheDatabase.checkTaggedFileUser(id,orig).isEmpty()); 
	}
	@Test
	public void testCheckTaggedFileUser() 
	{
		assertTrue(TheDatabase.checkTaggedFileUser(id+10,orig).isEmpty()); 
	}
}
