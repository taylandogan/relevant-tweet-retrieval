package twitter;

import java.util.concurrent.LinkedBlockingQueue;

import nlp.TextAnalyzer;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class CustomTwitterStream implements StatusListener {
	
	public TextAnalyzer textAnalyzer = null;
	
	public CustomTwitterStream() {
		super();
		this.textAnalyzer = new TextAnalyzer();
	}

	private LinkedBlockingQueue<Tweet> tweetQ = new LinkedBlockingQueue<>();

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
			String cleanTweet = textAnalyzer.extractWordList(rawTweet).toString();
			
			System.out.println("Raw Tweet: " + rawTweet);
			System.out.println("Clean Tweet: " + cleanTweet);
			
			// If tweet satisfies our requirements, add it to the queue
			if(isTweetNice(cleanTweet)) {
				Tweet t = new Tweet(cleanTweet, 
						rawTweet,
						st.getUser().getName(),
						st.getCreatedAt(),
						st.getRetweetCount(),
						st.getFavoriteCount());
				
				tweetQ.add(t);
			}
		}
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public LinkedBlockingQueue<Tweet> getTweetQ() {
		return tweetQ;
	}

	public void setTweetQ(LinkedBlockingQueue<Tweet> tweetQ) {
		this.tweetQ = tweetQ;
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
