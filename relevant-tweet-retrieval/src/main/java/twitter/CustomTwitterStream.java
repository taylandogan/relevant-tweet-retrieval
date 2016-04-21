package twitter;

import java.util.concurrent.LinkedBlockingQueue;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class CustomTwitterStream implements StatusListener {
	
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
			System.out.println(st.getText());
			Tweet t = new Tweet(
					st.getUser().getName(),
					st.getCreatedAt(),
					st.getRetweetCount(),
					st.getFavoriteCount(),
					st.getText());
			
			tweetQ.add(t);
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

}
