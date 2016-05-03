package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class H2ConnectionProvider {
	File configDirectory = new File("conf");
	private static final String dbConfigFileName = "dbConf.properties";
	
	private String driverClass = null;
	private String jdbcUrl = null;
	private String dbName = null;
	private String username = null;
	private String password = null;
	
	public H2ConnectionProvider() {
		
	}
	
	public Connection getH2Connection() throws SQLException {
		// Read DB config from .properties file
		readDbConfig(dbConfigFileName);
		// Attempt connecting to DB
		return DriverManager.getConnection(jdbcUrl, username, password);
	}
	
	// Reads the DB configs
	public void readDbConfig(String configFileName){
		Properties properties = new Properties();
		File configFile = new File(configDirectory.getAbsolutePath() + File.separator + configFileName);
		
		try {
			FileInputStream fis = new FileInputStream(configFile);
			InputStream is = fis;
			properties.load(is);
			
			this.driverClass = properties.getProperty("driver_class");
			this.jdbcUrl = properties.getProperty("jdbc_url");
			this.dbName = properties.getProperty("db_name");
			this.username = properties.getProperty("username");
			this.password = properties.getProperty("password");
			
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
		sb.append("Db configurations are read.\n");
		sb.append("#--Db Configurations--\n");
		sb.append(this.driverClass + "\n");
		sb.append(this.jdbcUrl + "\n");
		sb.append(this.dbName + "\n");
		sb.append(this.username + "\n");
		sb.append(this.password + "\n");
		sb.append("#----\n");
		System.out.println(sb.toString());
	}

	public static void main(String[] args) throws SQLException {
		
	}
}
