/*
* PURPOSE: Update portfolio item.
* 
* NOTES: When running,
* 1. Choose option 1 to see list of stocks.
* 2. Choose option 2 to create portfolio table.
* 3. Choose option 3 and add stocks using names from the previous list of stocks.
* 4. Choose option 4 and update one of your portfolio items.
*/

package Solutions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.intersystems.jdbc.IRISDataSource;

public class jdbcplaystocksTask5 {
	
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
		String host = map.get("host");
		int port = Integer.parseInt(map.get("port"));
		String namespace = map.get("namespace");
		String username = map.get("username");
		String password = map.get("password");
		
		try {
			// Using IRISDataSource to connect
			IRISDataSource ds = new IRISDataSource();

			// Create connection string
			String dbUrl = protocol + host + ":" + port + "/" + namespace;
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
					System.out.println("Creating table...");
					CreatePortfolioTable(dbconnection);
					break;
				case "3":
					System.out.print("Name: ");
					String name = scanner.next();
					System.out.print("Date: ");
					String tDate = scanner.next();
					System.out.print("Price: ");
					String price = scanner.next();
					System.out.print("Number of shares: ");
					int shares = scanner.nextInt();
					AddPortfolioItem(dbconnection,name,tDate,price,shares);
					break;
				case "4":
					System.out.print("Which stock would you like to update? ");
					String stockName = scanner.next();
					System.out.print("New Price: ");
					String updatePrice = scanner.next();
					System.out.print("New Date: ");
					String updateDate = scanner.next();
					System.out.print("New number of shares: ");
					int updateShares = scanner.nextInt();
					UpdateStock(dbconnection,stockName,updatePrice,updateDate, updateShares);
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

	// Create portfolio table
	public static void CreatePortfolioTable(Connection dbconnection) 
	{
		String createTable = "CREATE TABLE Demo.Portfolio(Name varchar(50) unique, PurchaseDate date, PurchasePrice numeric(10,4), Shares int, DateTimeUpdated datetime)";
		try 
		{
			Statement myStatement = dbconnection.createStatement();
			myStatement.executeUpdate(createTable);
			System.out.println("Created Demo.Portfolio table successfully.");
		} 
		catch (SQLException e) 
		{
			System.out.println("Table not created and likely already exists.");
			e.getMessage();
		}
	}

	// Add item to portfolio
	public static void AddPortfolioItem (Connection dbconnection, String name, String purchaseDate, String price, int shares)
	{
	    // Get current time
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try 
		{
			String sql = "INSERT INTO Demo.Portfolio (name,PurchaseDate,PurchasePrice,Shares,DateTimeUpdated) VALUES (?,?,?,?,?)";
			PreparedStatement myStatement = dbconnection.prepareStatement(sql);
			myStatement.setString(1, name);
			myStatement.setString(2, purchaseDate);
			myStatement.setString(3, price);
			myStatement.setInt(4, shares);
			myStatement.setString(5, timestamp.toString());
			myStatement.execute();
			System.out.println("Added new line item for stock " + name + ".");
		} 
		catch (SQLException e) 
		{
			// Error code 119 is uniqueness constraint violation
			if (e.getErrorCode() == 119) 
			{
				System.out.println(name + " is already in the portfolio.");
			}
			else 
			{
				System.out.println("Error adding portfolio item: " + e.getMessage());	
			}
		}
	}

	// Update item in portfolio
	public static void UpdateStock(Connection dbconnection, String stockname, String price, String transDate, int shares)
	{
		try 
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String sql = "UPDATE Demo.Portfolio SET purchaseDate = ?, purchasePrice= ?, shares = ?, DateTimeUpdated= ? WHERE name= ?";
			PreparedStatement myStatement = dbconnection.prepareStatement(sql);
			myStatement.setString(1, transDate);
			myStatement.setString(2, price );
			myStatement.setInt(3, shares);
			myStatement.setString(4, timestamp.toString());
			myStatement.setString(5, stockname);
			myStatement.execute();
			
			int count = myStatement.getUpdateCount();
			if (count > 0)
			{
				System.out.println(stockname + " updated.");
			}
			else 
			{
				System.out.println(stockname + " not found");
			}
		} 
		catch (SQLException e) 
		{
			System.out.println("Error updating " + stockname + " : " + e.getMessage());
		}
	}

	// Helper method: Get connection details from config file
	public static HashMap<String, String> getConfig(String filename) throws FileNotFoundException, IOException{
        // Initial empty map to store connection details
        HashMap<String, String> map = new HashMap<String, String>();

        String line;

        // Using Buffered Reader to read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(jdbcplaystocksTask5.class.getResourceAsStream(filename)));

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
	
