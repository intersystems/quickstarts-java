/*
* PURPOSE: Makes a connection to an instance of InterSystems IRIS Data Platform.
*/

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.intersystems.jdbc.IRISDataSource;

public class jdbcplaystocksTask1 {
	
	public static void main(String[] args) {	
	    // Initialize map to store connection details from config.txt
	    HashMap<String, String> map = new HashMap<String, String>();
		try{
			map = getConfig("config.txt");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}

		// Retrieve connection information from configuration file
		String protocol = "jdbc:IRIS://";
		String ip = map.get("ip");
		int port = Integer.parseInt(map.get("port"));
		String namespace = map.get("namespace");
		String username = map.get("username");
		String password = map.get("password");
		
		try {
			// Using IRISDataSource to connect
			IRISDataSource ds = new IRISDataSource();

			// Create connection string
			String dbUrl = protocol + ip + ":" + port + "/" + namespace;
			ds.setURL(dbUrl);
			ds.setUser(username);
			ds.setPassword(password);

			// Making connection
			Connection dbconnection = ds.getConnection();
			System.out.println("Connected to InterSystems IRIS via JDBC.");
    		dbconnection.close();
				
		}
		catch ( SQLException e) 
		{ 
			System.out.println(e.getMessage());
		} 
	}

	// Helper method: Get connection details from config file
	public static HashMap<String, String> getConfig(String filename) throws FileNotFoundException, IOException{
        // Initial empty map to store connection details
        HashMap<String, String> map = new HashMap<String, String>();

        String line;

        // Using Buffered Reader to read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(jdbcplaystocksTask1.class.getResourceAsStream(filename)));

        while ((line = reader.readLine()) != null)
        {
            // Remove all spaces and split line based on first colon
            String[] parts = line.replaceAll("\\s+","").split(":", 2);

            // Check if line contains enough information
            if (parts.length >= 2)
            {
                String key = parts[0];
                String value = parts[1];
                map.put(key, value);
            } else {
                System.out.println("Ignoring line: " + line);
            }
        }

        reader.close();

        return map;
    }
}
	
