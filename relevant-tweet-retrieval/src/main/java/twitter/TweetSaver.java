package twitter;

import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

import db.DbOperations;

public class TweetSaver extends Thread {
	
	public static LinkedBlockingQueue<Tweet> tweetQ = new LinkedBlockingQueue<Tweet>();
	private volatile boolean shouldIRun = true;
	
	public static void externalInsertToQ(Tweet t) {
		try {
			tweetQ.put(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(shouldIRun) {
			Tweet t = tweetQ.peek();
			
			// Attempt to save head of q to db, if the queue is not empty
			if(t != null) {
				try {
					t = tweetQ.take();
					DbOperations.insertTweet(t);
					System.out.println("Inserted: " + t.cleanText);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void terminate() {
		this.shouldIRun = false;
	}

}
