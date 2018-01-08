package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/** Simple command-line based search demo. */
public class SearchFiles {

  private static String fpath;	
  private SearchFiles() {}

  /** Simple command-line based search demo. */
  public static String search(String index, String field, String queries, boolean raw, int hitsPerPage) throws Exception {
    
	fpath = index;
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index+"index")));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer();

    BufferedReader in = null;
    QueryParser parser = new QueryParser(field, analyzer);
    queries = queries.trim();
    Query query = parser.parse(queries);
    System.out.println("Searching for: " + query.toString(field));
    searcher.search(query, 100);
    String res = doPagingSearch(in, searcher, query, hitsPerPage, raw);
    reader.close();
    return res;
  }

  /**
   * This demonstrates a typical paging search scenario, where the search engine presents 
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   * 
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   * 
   */
  public static String doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean raw) throws IOException {
 
    // Collect enough docs to show 5 pages
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;
    
    int numTotalHits = results.totalHits;
    System.out.println(numTotalHits + " total matching documents");
    String res = "";
    int counter = 0;
      for (int i = 0; i < numTotalHits; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          continue;
        }

        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
          System.out.println((i+1) + ". " + path);
          Path p = Paths.get(path);
          System.out.println(p.toString());
          String title = p.getFileName().toString();
          System.out.println("   Title: " + title);
          //This result is a collection of links
          //clicking each link will send a get request to DownloadServlet, which will 
          //return an outputstream of the file given the filepath as a param
          res += "<br>"+"<a href=DownloadServlet?filePath="+path+">"+title+"</a>"+"<button onclick=\"tagEvent(this)\" id=\"tagButton\" value=\"" + path +"\">Tag File</button><br>";
          
          //preview section
          //default generated preview path | taking only the name of file; removing extension
          
          String previewPath = fpath + "preview/" + title + ".txt";
          BufferedReader b = null;
          String allPreview = "";
          
          try{
        	  String tempLine;
        	  b = new BufferedReader(new FileReader(previewPath));
        	  
        	  while((tempLine = b.readLine()) != null){
        		  allPreview += tempLine;
        		  System.out.println(tempLine);;
        	  }
        	  
          }catch(IOException e){
        	  e.printStackTrace();
          }finally{
        	  try{
        		  if (b != null){
        			  b.close();
        		  }
        	  }catch(IOException e){
        		  e.printStackTrace();
        	  }
          }
          
          res += allPreview;
        } else {
          System.out.println((i+1) + ". " + "No path for this document");
        }
                
      }
      
      return res;
      
  }
}

