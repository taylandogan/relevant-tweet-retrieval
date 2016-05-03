package twitter;

import java.util.Date;

// A custom tweet class including the necessary fields only
public class Tweet {
	
	public String cleanText;
	public String rawText;
	public String username;
	public Date creationDate;
	public long retweetCount;
	public long favoriteCount;
	
	public Tweet(String cleanText, String rawText, String username,
			Date creationDate, long retweetCount, long favoriteCount) {
		super();
		this.cleanText = cleanText;
		this.rawText = rawText;
		this.username = username;
		this.creationDate = creationDate;
		this.retweetCount = retweetCount;
		this.favoriteCount = favoriteCount;
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

	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public long getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(long favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	
}
