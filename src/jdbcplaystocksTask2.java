/*
* PURPOSE: View top 10 stocks.
*
* NOTES: When running the application, choose option 1 and try 2016-08-12.
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.intersystems.jdbc.IRISDataSource;

public class jdbcplaystocksTask2 {
	
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
			
			// Starting interactive prompt
			boolean always = true;
			Scanner scanner = new Scanner(System.in);
			while (always) {
				System.out.println("1. View top 10");
				System.out.println("2. Create Portfolio table");
				System.out.println("3. Add to Portfolio");
				System.out.println("4. Update Portfolio");
				System.out.println("5. Delete from Portfolio");
				System.out.println("6. View Portfolio");
				System.out.println("7. Quit");
				System.out.print("What would you like to do? ");

				String option = scanner.next();
				switch (option) {
				case "1":
					System.out.print("On which date? (YYYY-MM-DD) ");
					String queryDate = scanner.next();
					FindTopOnDate(dbconnection, queryDate);
					break;
				case "2":
					System.out.println("TO DO: Create Portfolio table");
					break;
				case "3":
					System.out.println("TO DO: Add to Portfolio");
					break;
				case "4":
					System.out.println("TO DO: Update Portfolio");
					break;
				case "5":
					System.out.println("TO DO: Delete from Portfolio");
					break;
				case "6":
					System.out.println("TO DO: View Portfolio");
					break;
				case "7":
					System.out.println("Exited.");
					always = false;
					break;
				default: 
					System.out.println("Invalid option. Try again!");
					break;
				}			
			}
			scanner.close();
			dbconnection.close();		
		}
		catch ( SQLException e) 
		{ 
			System.out.println(e.getMessage());
		} 
	}

	// Find top 10 stocks on a particular date
	public static void FindTopOnDate(Connection dbconnection, String onDate)
	{
		try 
		{
			String sql = "SELECT distinct top 10 transdate,name,stockclose,stockopen,high,low,volume FROM Demo.Stock WHERE transdate= ? ORDER BY stockclose desc";
			PreparedStatement myStatement = dbconnection.prepareStatement(sql);
			myStatement.setString(1, onDate);
			ResultSet myRS = myStatement.executeQuery();
			
			System.out.println("Date\t\tName\tOpening Price\tDaily High\tDaily Low\tClosing Price\tVolume");
			while (myRS.next()) 
			{
	            Date date = myRS.getDate("TransDate");
	            BigDecimal open = myRS.getBigDecimal("StockOpen");
	            BigDecimal high = myRS.getBigDecimal("High");
	            BigDecimal low = myRS.getBigDecimal("Low");
	            BigDecimal close = myRS.getBigDecimal("StockClose");
	            int volume = myRS.getInt("Volume");
	            String name = myRS.getString("Name");
	          
	            System.out.println(date + "\t" + name + "\t" + open + "\t" + high+ "\t" + low + "\t" + close+ "\t" + volume);
	        }
		}
		catch (SQLException e)
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(jdbcplaystocksTask2.class.getResourceAsStream(filename)));

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
	
