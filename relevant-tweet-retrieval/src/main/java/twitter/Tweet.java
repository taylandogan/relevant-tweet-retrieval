package twitter;

import java.util.Date;

// A custom tweet class including the necessary fields only
public class Tweet {
	
	public long tweetId;
	public String cleanText;
	public String rawText;
	public String username;
	public Date creationDate;
	public int retweetCount;
	public int favoriteCount;
	
	public Tweet(long tweetId, String cleanText, String rawText, String username,
			Date creationDate, int retweetCount, int favoriteCount) {
		super();
		this.tweetId = tweetId;
		this.cleanText = cleanText;
		this.rawText = rawText;
		this.username = username;
		this.creationDate = creationDate;
		this.retweetCount = retweetCount;
		this.favoriteCount = favoriteCount;
	}
	
	public long getTweetId() {
		return tweetId;
	}
	
	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public String getCleanText() {
		return cleanText;
	}

	public void setCleanText(String cleanText) {
		this.cleanText = cleanText;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public long getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public long getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	
}
