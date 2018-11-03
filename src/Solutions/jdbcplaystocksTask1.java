package Solutions;

import java.sql.Connection;
import java.sql.SQLException;

import com.intersystems.jdbc.IRISDataSource;

public class jdbcplaystocksTask1 {
	
	public static void main(String[] args) {
		String dbUrl = "jdbc:IRIS://127.0.0.1:51773/USER";
		String user = "superuser";
		String pass = "SYS";
		
		try {
			//Making connection
			IRISDataSource ds = new IRISDataSource(); 
			ds.setURL(dbUrl);
			ds.setUser(user);
			ds.setPassword(pass);
			Connection dbconnection = ds.getConnection();
			System.out.println("Connected to InterSystems IRIS via JDBC.");
			
			
			//Future code here
			dbconnection.close();
				
		}
		catch ( SQLException e) 
		{ 
			System.out.println(e.getMessage());
		} 
	}
	
}
	
