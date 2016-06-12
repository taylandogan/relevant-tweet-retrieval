package ir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexSearch {
	
	private Analyzer analyzer;
	private DirectoryReader dReader;
	private IndexSearcher iSearcher;
	Directory indexDirectory;
	File indexFileDir = new File("indexdir");
	
	public LuceneIndexSearch() {
		try {
			readIndex();
		} catch (IOException e) {
			System.out.println("Could not read the index: " + e.getMessage());
		}
	}
	
	public void readIndex() throws IOException {
		// Read the index directory
		Path pathToIndex = indexFileDir.toPath();
		indexDirectory = FSDirectory.open(pathToIndex);
		dReader = DirectoryReader.open(indexDirectory);
		
		// Bind directory to searcher
		iSearcher = new IndexSearcher(dReader);
		
		// Set the analyzer
		analyzer = new StandardAnalyzer();
	}
	
	public List<Long> searchIndex(String queryText, String fieldName) {
		System.out.println("Query terms:" + queryText);
		// Create a query parser
		QueryParser qParser = new QueryParser(fieldName, analyzer);
		
		// Resulting ID list
		List<Long> hitIdList = new ArrayList<Long>();
		
		// Attempt parsing and searching the given query text
		try {
			Query q = qParser.parse(queryText);
			ScoreDoc[] hits = iSearcher.search(q, 20).scoreDocs;
			System.out.println("Length of hit list: " + hits.length);
			
			// Collect hit ids
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = iSearcher.doc(hits[i].doc);
				String hitId = hitDoc.get("id").toString();
		      
				if(hitId != null) {
					hitIdList.add(Long.parseLong(hitId));
				}     
			}
			
		} catch (ParseException e) {
			System.out.println("Could not parse the given query text: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error while searching index: " + e.getMessage());
		}
		
		return hitIdList;
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		dReader.close();
		indexDirectory.close();
	}
	
}
