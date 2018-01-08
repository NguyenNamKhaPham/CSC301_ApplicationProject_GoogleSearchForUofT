package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String filePath = request.getParameter("filePath");
		if(filePath == null || filePath.equals("")){
			throw new ServletException("FilePath can't be null or empty");
		}
		
		File file = new File(filePath);
		if(!file.exists()){
			throw new ServletException("File does not exist.");
		}
		//load file into inputstream
		InputStream fis = new FileInputStream(file);
		response.setContentLength((int) file.length());
		//set the header with the correct filename 
		//so when prompted to download it gives you the correct filename + ext
		response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
		//generate new ouputstream which will hold the file
		//this outputstream is the response to the getrequest
		ServletOutputStream os = response.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read=0;
		//loop through inputstream writing it to the outputstream
		//read fis to bufferdata 1024bytes at a time
		while((read = fis.read(bufferData))!= -1){
			//write to bufferdata to os 1024 bytes at a time
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
		System.out.println("File loaded by client");
	}

}
