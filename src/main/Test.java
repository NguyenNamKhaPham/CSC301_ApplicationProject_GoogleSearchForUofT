package main;

public class Test {
		public static void main(String[] args){
			String index = "/Users/phamkha/Dropbox (Kha_John)/UTM/Year3/CSC301/Project/301grouprepository2/index";
	    	String field = "contents";
	    	String queries = "week4.pdf";
	    	boolean raw = false;
	    	int hitsPerPage = 10;
	    	
	       try {
	    	   SearchFiles.search(index, field, queries, raw, hitsPerPage);
	       } catch (Exception e) {
	    	   e.printStackTrace();
	       }
		}
	
}
