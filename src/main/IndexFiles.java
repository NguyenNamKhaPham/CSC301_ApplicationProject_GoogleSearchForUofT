package main;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import java.io.*;
import java.util.Date;
import java.nio.file.*;

/** Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing.
 * Run it with no command-line arguments for usage information.
 */
public class IndexFiles {
  
  private IndexFiles() {}
//change this only for test classes
  public static String indexPath = "/home/kosar/Desktop/proj/301grouprepository2/301Project/index/";
  public static String docsPath = "/home/kosar/Desktop/proj/301grouprepository2/301Project/uploadFile/"; 
  /** Index all text files under a directory. */
  public static void main(String[] args) {
    String usage = "java org.apache.lucene.demo.IndexFiles"
                 + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
                 + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                 + "in INDEX_PATH that can be searched with SearchFiles";

    boolean create = true;
    for(int i=0;i<args.length;i++) {
      if ("-index".equals(args[i])) {
        indexPath = args[i+1];
        i++;
      } else if ("-docs".equals(args[i])) {
        docsPath = args[i+1];
        i++;
      } else if ("-update".equals(args[i])) {
        create = false;
      }
    }

    if (docsPath == null) {
      System.err.println("Usage: " + usage);
      System.exit(1);
    }
    
    final File docDir = new File(docsPath);
    if (!docDir.exists() || !docDir.canRead()) {
      System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
      System.exit(1);
    }
    
    Date start = new Date();
    try {
      System.out.println("Indexing to directory '" + indexPath + "'...");
      Path p = Paths.get(indexPath);
      
      Directory dir = FSDirectory.open(p);
      Analyzer analyzer = new StandardAnalyzer();
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

      if (create) {
        // Create a new index in the directory, removing any
        // previously indexed documents:
        iwc.setOpenMode(OpenMode.CREATE);
      } else {
        // Add new documents to an existing index:
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
      }

      // Optional: for better indexing performance, if you
      // are indexing many documents, increase the RAM
      // buffer.  But if you do this, increase the max heap
      // size to the JVM (eg add -Xmx512m or -Xmx1g):
      //
      // iwc.setRAMBufferSizeMB(256.0);

      IndexWriter writer = new IndexWriter(dir, iwc);
      indexDocs(writer, docDir);

      // NOTE: if you want to maximize search performance,
      // you can optionally call forceMerge here.  This can be
      // a terribly costly operation, so generally it's only
      // worth it when your index is relatively static (ie
      // you're done adding documents to it):
      //
      // writer.forceMerge(1);

      writer.close();

      Date end = new Date();
      System.out.println(end.getTime() - start.getTime() + " total milliseconds");

    } catch (IOException e) {
      System.out.println(" caught a " + e.getClass() +
       "\n with message: " + e.getMessage());
    }
  }

  /**
   * Indexes the given file using the given writer, or if a directory is given,
   * recurses over files and directories found under the given directory.
   * 
   * NOTE: This method indexes one document per input file.  This is slow.  For good
   * throughput, put multiple documents into your input file(s).  An example of this is
   * in the benchmark module, which can create "line doc" files, one document per line,
   * using the
   * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
   * >WriteLineDocTask</a>.
   *  
   * @param writer Writer to the index where the given file/dir info will be stored
   * @param file The file to index, or the directory to recurse into to find files to index
   * @throws IOException If there is a low-level I/O error
   */
  static void indexDocs(IndexWriter writer, File file)
    throws IOException {
    // do not try to index files that cannot be read
    if (file.canRead()) {
      if (file.isDirectory()) {
        String[] files = file.list();
        // an IO error could occur
        if (files != null) {
          for (int i = 0; i < files.length; i++) {
            indexDocs(writer, new File(file, files[i]));
          }
        }
      }

      else {
        FileInputStream fis;
        try {
          fis = new FileInputStream(file);
        } catch (FileNotFoundException fnfe) {
          return;
        }

        try {
          // make a new, empty document
          Document doc = new Document();
          
          // Add the path of the file as a field named "path". 
          Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
          doc.add(pathField);
          
          // Add the name of the file as a field named "file name". 
          Field fileNameField = new StringField("file name", file.getName(), Field.Store.YES);
          System.out.println(file.getName());
          doc.add(fileNameField);
          
          // If the file is a pdf, load contents of the pdf and add it to the document
          if(file.getName().endsWith(".pdf")) {
            String contents = "";
            PDDocument pdoc = null;
            try {
              pdoc = PDDocument.load(file);
              PDFTextStripper stripper = new PDFTextStripper();

              stripper.setLineSeparator("\n");
              stripper.setStartPage(1);
              stripper.setEndPage(5);// this mean that it will index the first 5 pages only
              contents = stripper.getText(pdoc);
             
            } catch(Exception e){
              e.printStackTrace();
            }
            doc.add(new TextField("contents", contents, Field.Store.NO));
            pdoc.close();
          }

          // Add the last modified date of the file a field named "modified".
          doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));

          // Add the contents of the file to a field named "contents".  
          doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));

          if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
            // New index, so we just add the document (no old document can be there):
            System.out.println("adding " + file);
            writer.addDocument(doc);
          } else {
            System.out.println("updating " + file);
            writer.updateDocument(new Term("path", file.getPath()), doc);
          }
          
        } finally {
          fis.close();
        }
      }
    }
  }
  //method for 
  public static void deleteFromIndex(String name) throws IOException
  {
      Path p = Paths.get(indexPath);
      Term term = new Term("path" , docsPath+name);

      Directory dir = FSDirectory.open(p);
      Analyzer analyzer = new StandardAnalyzer();
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
      iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
      
      IndexWriter writer = new IndexWriter(dir, iwc);
      writer.deleteDocuments(term);
      writer.close();
      
  }
}
