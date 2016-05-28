package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter.Tweet;

/*
 * Tweets table description is as follows:
 * 
CREATE TABLE TWEETS(
	ID BIGINT PRIMARY KEY NOT NULL, 
	CLEAN VARCHAR(140), 
	RAW VARCHAR(140), 
	USERNAME VARCHAR(50), 
	CRDATE TIMESTAMP, -- creation date
	RTC INT, -- retweet count
	FAVC INT --favorite count
);


Insertion:
INSERT INTO TWEETS VALUES(null, 'clean tweet', 'this is a raw tweet', 'taylan', '2016-05-20 19:22:05', 2, 5);

*/


public class DbOperations {
	
	//INSERT INTO TEST VALUES(1, 'Hello');
	//SELECT * FROM TEST ORDER BY ID;
	//DELETE FROM TEST WHERE ID=2;
	
	public static void insertTweet(Tweet tweet) throws SQLException {
		// Get DB connection
		H2ConnectionProvider h2 = new H2ConnectionProvider();
		Connection connection = h2.getH2Connection();
	
		// Prepare statement and query
		PreparedStatement insertStmt = null;
		String insertQuery = "INSERT INTO TWEETS" + "(id, clean, raw, username, crdate, rtc, favc) VALUES" + "(null,?,?,?,?,?,?)";
	
		// Convert java.util.Date to java.sql.Timestamp
		Timestamp ts = new Timestamp(tweet.creationDate.getTime());
		
		// Set parameters
		insertStmt = connection.prepareStatement(insertQuery);
		insertStmt.setString(1, tweet.cleanText);
		insertStmt.setString(2, tweet.rawText);
		insertStmt.setString(3, tweet.username);
		insertStmt.setTimestamp(4, ts);
		insertStmt.setInt(5, tweet.retweetCount);
		insertStmt.setInt(6, tweet.favoriteCount);
		
		// Execute query
		insertStmt.executeUpdate();
		insertStmt.close();
	
		// Commit and close connection
		connection.commit();
		connection.close();
	}
	
	public static List<Tweet> getAllTweetsFromDB() throws SQLException {
		List<Tweet> tweetsOnDB = new ArrayList<Tweet>();
		
		// Get DB connection
		H2ConnectionProvider h2 = new H2ConnectionProvider();
		Connection connection = h2.getH2Connection();
	
		// Prepare statement and query
		PreparedStatement selectStmt = null;
		String selectQuery = "SELECT * FROM TWEETS";
		
		selectStmt = connection.prepareStatement(selectQuery);
		ResultSet rs = selectStmt.executeQuery();
		
		while (rs.next()) {
			Tweet t = new Tweet(
					rs.getInt("id"), 
					rs.getString("clean"), 
					rs.getString("raw"), 
					rs.getString("username"), 
					rs.getDate("crdate"),
					rs.getInt("rtc"), 
					rs.getInt("favc")
			);
			
			tweetsOnDB.add(t);
		}
		
		selectStmt.close();
		
		// Commit and close connection
		connection.commit();
		connection.close();
		
		return tweetsOnDB;
	}

	public static void main(String[] args) throws SQLException {
		long l = -1;
		Date d = new java.util.Date();
		Tweet t = new Tweet(l, "insert from java", "this is an insert from java", "java3", new Timestamp(d.getTime()), 6, 10);
		DbOperations.insertTweet(t);
		System.out.println("DONE");
		List<Tweet> tweetList = DbOperations.getAllTweetsFromDB();
	
		for(int i = 0; i < tweetList.size(); i++) {
			System.out.println(tweetList.get(i).getUsername());
		}
	}

}
