package twitter;

import java.sql.SQLException;
import java.util.List;

import db.DbOperations;
import nlp.TextAnalyzer;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class CustomTwitterStream implements StatusListener {
	
	public TextAnalyzer textAnalyzer = null;
	//public TweetSaver tweetSaver = null;
	
	public CustomTwitterStream() {
		super();
		this.textAnalyzer = new TextAnalyzer();
		//this.tweetSaver = new TweetSaver();
		//this.tweetSaver.start();
	}
	
	@Override
	public void onException(Exception e) {
		System.out.println(e);
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(Status st) {
		// If tweet is in english, create & enqueue the tweet
		if(st.getLang().equalsIgnoreCase("en")) {
			String rawTweet = st.getText();
			List<String> extractedWordList = textAnalyzer.extractWordList(rawTweet);
			String cleanTweet = String.join(" ", extractedWordList);
			
			//System.out.println("Raw Tweet: " + rawTweet);
			//System.out.println("Clean Tweet: " + cleanTweet);
			
			// If tweet satisfies our requirements, add it to the queue
			if(isTweetNice(cleanTweet)) {
				Tweet t = new Tweet(
						-1, //id before putting DB, the id given by DB will be used later
						cleanTweet, 
						rawTweet,
						st.getUser().getName(),
						st.getCreatedAt(),
						st.getRetweetCount(),
						st.getFavoriteCount());
			
				TweetSaver.externalInsertToQ(t);

//				try {
//					DbOperations.insertTweet(t);
//					System.out.println("Inserted: " + cleanTweet);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}

	
	// Implement this to your liking, e.g. I want my clean tweet to be long enough 
	public boolean isTweetNice(String cleanTweet) {
		
		// If clean tweet length is shorter than 50 chars
		if(cleanTweet.length() < 50) {
			return false;
		}
		
		return true;
	}

}
