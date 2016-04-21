package twitter;

import java.util.Date;

// A custom tweet class including the necessary fields only
public class Tweet {
	
	public String username;
	public Date creationDate;
	public long retweetCount;
	public long favoriteCount;
	public String text;
	
	public Tweet(String username, Date creationDate, long retweetCount,
			long favoriteCount, String text) {
		super();
		this.username = username;
		this.creationDate = creationDate;
		this.retweetCount = retweetCount;
		this.favoriteCount = favoriteCount;
		this.text = text;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
