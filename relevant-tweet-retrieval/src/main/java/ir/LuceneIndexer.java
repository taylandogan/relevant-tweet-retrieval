package ir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import db.H2ConnectionProvider;

public class LuceneIndexer {

	private IndexWriter iWriter;
	Directory indexDirectory;
	File indexFileDir = new File("indexdir");
	
	public LuceneIndexer() throws IOException {
		// Declare the index directory
		Path pathToIndex = indexFileDir.toPath();
		indexDirectory = FSDirectory.open(pathToIndex);
		
		// Define the analyzer
		Analyzer analyzer = new StandardAnalyzer();
		
		// Create index writer config
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE); //If there is an index, append. Otw create a new one.
		
		// Initialize the index writer
		this.iWriter = new IndexWriter(indexDirectory, iwc);
	}
	
	public void indexDatabaseEntries() throws SQLException, IOException {
		// Get DB connection
		H2ConnectionProvider h2 = new H2ConnectionProvider();
		Connection connection = h2.getH2Connection();
	
		// Prepare statement and query
		PreparedStatement selectStmt = null;
		String selectQuery = "SELECT id, clean, raw FROM TWEETS";
		
		selectStmt = connection.prepareStatement(selectQuery);
		ResultSet rs = selectStmt.executeQuery();
		
		// Declare field types
		
		FieldType idFT = new FieldType(); // A field type for ID
		idFT.setIndexOptions(IndexOptions.NONE); // Do not index id information
		idFT.setTokenized(true); // Do not apply tokenization
		idFT.setStored(true); // Store id values
		idFT.freeze(); // Prevents further changes
		
		FieldType cleanTweetFT = new FieldType(); // A field type for clean tweets
		cleanTweetFT.setIndexOptions(IndexOptions.DOCS_AND_FREQS); // Index documents and term frequencies
		cleanTweetFT.setTokenized(true); // Apply tokenization
		cleanTweetFT.setStored(true); // To store the value of field
		//cleanTweetFT.setOmitNorms(true); // By setting this option, we use raw tf
		cleanTweetFT.freeze(); // Prevents further changes
		
		
		while (rs.next()) {
			String cleanTweet = rs.getString("raw");
			//System.out.println(cleanTweet);
			Document d = new Document();
			d.add(new Field("id", rs.getBytes("id"), idFT));
			d.add(new Field("clean", cleanTweet, cleanTweetFT));
			iWriter.addDocument(d);
		}
		
		// Commit (and automatically close, check writer configs) writer & close dir
		iWriter.commit();
		indexDirectory.close();
		
		// Close statement
		selectStmt.close();
		
		// Commit and close connection
		connection.commit();
		connection.close();
	}
	
	public void createIndex(){
		try {
			indexDatabaseEntries(); // Use database entries
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error while creating the index: " + e.getMessage());
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		long a = System.currentTimeMillis();;
		LuceneIndexer li = new LuceneIndexer();
		li.createIndex();
		long b = System.currentTimeMillis();;
		
		LuceneIndexSearch lis = new LuceneIndexSearch();
		/*
		System.out.println("----------------------");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Search Results: ");
		System.out.println();
		*/
		lis.searchIndex("donald trump", "clean");
		long c = System.currentTimeMillis();;
		
		System.out.println();
		System.out.println("Building the index took: " + (b-a) + " ms.");
		System.out.println("Searching the index took: " + (c-b) + " ms.");
		
	}

}
