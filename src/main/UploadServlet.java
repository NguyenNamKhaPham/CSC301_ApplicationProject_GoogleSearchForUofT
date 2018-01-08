package main;

//got help from "www.tutorialspoint.com/servlets/servlets-file-uploading.html"
//for generating this code
// Import required java libraries
import java.io.*;
import java.sql.SQLException;
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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class UploadServlet extends HttpServlet {
   
   private boolean isMultipart;
   private String filePath;
   private int maxFileSize = 500 * 1024;
   private int maxMemSize = 4 * 1024;
   private File file ;

   public void init( ){
      // Get the file location where it would be stored. the destination
      filePath = getServletContext().getInitParameter("file-upload"); 
   }
   
   public void doPost(HttpServletRequest request,HttpServletResponse response)
              throws ServletException, java.io.IOException {
	  
	  Cookie ck[] = request.getCookies();
	  int userId;
	  if(ck != null)
	  {
		  userId = Integer.parseInt(ck[0].getValue());
		  //System.out.println(ck[0].getValue());
	  }
	  else
	  {
		  userId = -10;
		  System.out.println("it didnt get the cookie");
	  }
      // Check that we have a file upload request
      isMultipart = ServletFileUpload.isMultipartContent(request);
      
      response.setContentType("text/html");
      java.io.PrintWriter out = response.getWriter( );
      
      if( !isMultipart ){

         out.println("No file uploaded<br>"); 

         return;
      }
      
      DiskFileItemFactory factory = new DiskFileItemFactory();
      // maximum size that will be stored in memory
      //increasing?
      factory.setSizeThreshold(maxMemSize);
      // Location to save data that is larger than maxMemSize.


      factory.setRepository(new File(filePath+"uploadFile"));


      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      // maximum file size to be uploaded.
      upload.setSizeMax( maxFileSize );

      try{ 
    	  // Parse the request to get file items.
    	  List fileItems = upload.parseRequest(request);
	
    	  // Process the uploaded file items
    	  Iterator i = fileItems.iterator();
      
    	  while ( i.hasNext () ) 
    	  {
    		  FileItem fi = (FileItem)i.next();
    		  if ( !fi.isFormField () )	
    		  {
    			  // Get the uploaded file parameters
    			  String fieldName = fi.getFieldName();
    			  String fileName = fi.getName();
    			  String contentType = fi.getContentType();
    			  boolean isInMemory = fi.isInMemory();
    			  long sizeInBytes = fi.getSize();
            
    			  // Write the file
    			      			  
    			  if( fileName.lastIndexOf("\\") >= 0 ){
    				  file = new File( filePath+"uploadFile/" + 
    						  fileName.substring( fileName.lastIndexOf("\\"))) ;
    				  //insert the file path with user id in database
    				  TheDatabase.insertFile(filePath +"uploadFile/"+ 
    						  fileName.substring( fileName.lastIndexOf("\\")), userId, filePath);
    			  }else{
    				  file = new File( filePath +"uploadFile/"+ 
    						  fileName.substring(fileName.lastIndexOf("\\")+1)) ;
    				  //insert file path with user id in database
    				  TheDatabase.insertFile(filePath +"uploadFile/"+ 
    						  fileName.substring( fileName.lastIndexOf("\\")+1), userId, filePath);
    			  }
    			  fi.write( file ) ;
    			  //response.addCookie(ck[0]);
    			  
    			  request.getRequestDispatcher("File.html").include(request, response);

    			  //indexing files
    			  String ind = filePath + "index/";
    			  String up = filePath + "uploadFile/";
    			  String [] temp = {"-index",ind,"-docs",up};
    			  IndexFiles.main(temp);
    			  
    			  out.println("<div name=\"res\" class='frame' style='color: red;'>Uploaded Filename: " + fileName + "</div>");
    			  createPreview(file);
    			  //response.sendRedirect("UploadFile.html");
    		  }
    	  }

      }catch(SQLException ex) {
      	  request.getRequestDispatcher("File.html").include(request, response);
    	  out.println("<div class='frame' style='color: red;'>File was not uploaded</div>");
    	  //response.sendRedirect("UploadFile.html");
    	  //return;
      } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   public void doGet(HttpServletRequest request, 
                       HttpServletResponse response)
        throws ServletException, java.io.IOException {
        
        throw new ServletException("GET method used with " +
                getClass( ).getName( )+": POST method required.");
   } 
   
   public void createPreview(File file){
       try {
    	   String contents = "";
    	   String path = filePath+"preview/";
    	   String filename = file.getName();
    	   if(file.getName().endsWith(".pdf")) {
    		   PDDocument pdoc = null;

    		   pdoc = PDDocument.load(file);
    		   PDFTextStripper stripper = new PDFTextStripper();

    		   stripper.setLineSeparator("<br>");
    		   stripper.setStartPage(1);
    		   stripper.setEndPage(1);
    		   contents = stripper.getText(pdoc);
    		   pdoc.close();
    		   
    		   
    		   //filename = filename.substring(0, filename.length() - 4);
    		   
    		   
    		   
    		   path += filename + ".txt";
    		   }
    	   else{
    		   BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
    		   String line = "";
    		   int i = 10;
    		   while ((line = br.readLine()) != null && i > 0) {
    			   contents += line + "<br>";
    			   i --;
    			   }
    		   br.close();
    		   
    		   if(file.getName().endsWith(".txt")) {
    			   
    			   path += filename+".txt";
    		   }else{
    			   path += filename;
    		   }
    	   }
	   
    	   PrintWriter writer = new PrintWriter(path, "UTF-8");
    	   
    	   if(contents.length() < 200){
    		   writer.println("<div class='preview'>" + contents +"</div>");
        	   writer.close();
    	   }else{
    	   
	    	   writer.println("<div class='preview'>" + contents.substring(0, 200) + "...</div>");
	    	   writer.close();
    	   }
       } catch(Exception e){
           e.printStackTrace();
         }
   }
}
