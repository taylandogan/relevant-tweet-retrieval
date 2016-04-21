package twitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetCollector extends Thread {
	File configDirectory = new File("conf");
	private static final String twitterConfigFileName = "twitterAuth.properties";
	private volatile boolean shouldIRun = true;
	
	private TwitterStream mainStream = null;
	private FilterQuery streamFilter = null;
	
	private String consumerKey = null;
	private String consumerSecret = null;
	private String accessToken = null;
	private String accessTokenSecret = null;
	
	public TweetCollector() {
		// Read auth configurations
		readTwitterConfig(twitterConfigFileName);
		// Initalizes a stream using CustomTwitterStream
		initalizeTwitterStream();
	}
	
	// Reads the twitter auth configs
	public void readTwitterConfig(String configFileName){
		Properties properties = new Properties();
		File configFile = new File(configDirectory.getAbsolutePath() + File.separator + twitterConfigFileName);
		
		try {
			FileInputStream fis = new FileInputStream(configFile);
			InputStream is = fis;
			properties.load(is);
			
			this.consumerKey = properties.getProperty("consumer_key");
			this.consumerSecret = properties.getProperty("consumer_secret");
			this.accessToken = properties.getProperty("access_token");
			this.accessTokenSecret = properties.getProperty("access_token_secret");
			
			if(is != null) {
				is.close();
			}
				
			if(fis != null) {
				fis.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Twitter configuration is read.\n");
		sb.append("#--Auth Configurations--\n");
		sb.append(this.consumerKey + "\n");
		sb.append(this.consumerSecret + "\n");
		sb.append(this.accessToken + "\n");
		sb.append(this.accessTokenSecret + "\n");
		sb.append("#----\n");
		System.out.println(sb.toString());
	}
	
	public void initalizeTwitterStream() {
		assert consumerKey != null;
		assert consumerSecret != null;
		assert accessToken != null;
		assert accessTokenSecret != null;
		
		 ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
	        configurationBuilder.setOAuthConsumerKey(consumerKey)
	                .setOAuthConsumerSecret(consumerSecret)
	                .setOAuthAccessToken(accessToken)
	                .setOAuthAccessTokenSecret(accessTokenSecret);
	        
	   mainStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
	   mainStream.addListener(new CustomTwitterStream());

	   // Longitudes & latitudes for NYC.
	   double [][] location ={{-74,40},{-73,41}};
	   
	   streamFilter = new FilterQuery();
	   streamFilter.locations(location);
	   System.out.println("Twitter stream is initalized.");
	}
	
	public synchronized void run(){
		if (shouldIRun) {
			mainStream.filter(streamFilter);
		}	
	}
	
	public void terminate() {
		this.shouldIRun = false;
	}

	public static void main(String[] args) throws InterruptedException {
		TweetCollector tc = new TweetCollector();
		
		if (tc != null) {
			System.out.println("Tweet Collector thread is started.");
			tc.start();
			
			// TODO: Thread does not stop for now, fix it
			Thread.sleep(10000);
//			ExecutorService executor = Executors.newSingleThreadExecutor();
//			executor.invokeAll(Arrays.asList(new TweetCollector()), 10, TimeUnit.SECONDS); // Timeout of 10 seconds.
//			executor.shutdown();
			
			tc.terminate();
			tc.join();
			System.out.println("Tweet Collector thread is stopped.");
		}
	}
}
