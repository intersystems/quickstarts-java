/*
* PURPOSE: Store stock data directly to InterSystems IRIS Data Platform using a custom structure and generate trade data
*   with methods from InterSystems IRIS.
* 
* NOTES: When running,
* 1. Choose option 2 to store stock data natively.
* 2. Choose option 3 to retrieve stock data natively.
* 3. Choose option 4 to generate trades with random data using methods from InterSystems IRIS.
*/

package Solutions;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.intersystems.jdbc.IRISConnection;
import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISIterator;
import com.intersystems.jdbc.IRISDataSource;

import Solutions.Demo.Trade;

public class nativeplaystocksTask4 {

	public static void main(String[] args) {
		// Initialize map to store connection details from config.txt
	    HashMap<String, String> map = new HashMap<String, String>();
		try{
			map = getConfig("config.txt");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}

		// Retrieve connection information
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
			IRISConnection dbconnection = (IRISConnection) ds.getConnection();
			System.out.println("Connected to InterSystems IRIS via JDBC.");
					
			IRIS irisNative = IRIS.createIRIS(dbconnection);
					
			boolean always = true;
			Scanner scanner = new Scanner(System.in);
			while (always) {
				System.out.println("1. Test");
				System.out.println("2. Store stock data");
				System.out.println("3. View stock data");
				System.out.println("4. Generate Trades");
				System.out.println("5. Quit");
				System.out.print("What would you like to do? ");

				String option = scanner.next();
				switch (option) {
				case "1":
					SetTestGlobal(irisNative);
					break;
				case "2":
					StoreStockData(irisNative, dbconnection);
					break;
				case "3":
					System.out.println("Printing nyse globals...");

					// Get current time
					Long startPrint = System.currentTimeMillis();

					PrintNodes(irisNative, "nyse");

					// Calculate execution time
					Long totalPrint = System.currentTimeMillis() - startPrint;
					System.out.println("Execution time: " + totalPrint + "ms");
					break;
				case "4":
					GenerateData(irisNative,10);
					break;
				case "5":
					System.out.println("Exited.");
					always = false;
					break;
				default: 
					System.out.println("Invalid option. Try again!");
					break;
				}				
			}
			scanner.close();					
			irisNative.close();
	
		}
		catch (SQLException e)
		{ 
			System.out.println("SQL error in application: " + e.getMessage());
		} 
		catch (Exception e) 
		{
			System.out.println("Error - Exception thrown: " + e.getMessage());
		} 
	}

	// Write to a test global
	public static void SetTestGlobal(IRIS irisNative)
	{
		irisNative.set(8888, "^testglobal", "1");
		Integer globalValue = irisNative.getInteger("^testglobal", "1");
		System.out.println("The value of ^testglobal(1) is " + globalValue);
	}

	// Store stock data directly into InterSystems IRIS
	public static void StoreStockData(IRIS irisNative, IRISConnection dbconnection)
	{
		// Clear global from previous runs
		irisNative.kill("^nyse");
		System.out.println("Storing stock data using Native API...");
		
		// Get stock data using JDBC and write global
		try {
			Statement myStatement = dbconnection.createStatement(); //needed for JDBC if doing SQL side-by-side
			ResultSet myRS = myStatement.executeQuery("select top 1000 transdate,name,stockclose,stockopen,high,low,volume from Demo.Stock");

		    // Add stock data to list
			ArrayList<String> x = new ArrayList<>();
			while (myRS.next())
			{
				x.add(String.join(",", myRS.getString("name"), myRS.getString("transdate"), myRS.getString("high"), myRS.getString("low"), myRS.getString("stockopen"), myRS.getString("stockclose"), myRS.getString("volume")));					
			}
			int id=x.size();

			// Get current time
			Long startConsume = System.currentTimeMillis();

			// Loop through list of stock
			for (int i=0;i<id;i++)
			{
				irisNative.set(x.get(i),"^nyse",i+1);		
			}

	        // Get time consuming
			Long totalConsume = System.currentTimeMillis() - startConsume;
			System.out.println("Stored natively successfully. Execution time: " + totalConsume + "ms");

			myStatement.close();
		} 
		catch (SQLException e) {
			System.out.println("Error either retrieving data using JDBC or storing to globals:" + e.getMessage());
		} 
	}

	// Iterate over all nodes forwards and print
	public static void PrintNodes(IRIS irisNative, String globalName)
	{
		System.out.println("Iterating over " + globalName + " globals");

		// Create iter
		IRISIterator iter = irisNative.getIRISIterator(globalName);

		// Iterate over all nodes forwards
		System.out.println("walk forwards");
		while (iter.hasNext()) {
			String subscript = iter.next();
			System.out.println("subscript=" + subscript + ", value=" + iter.getValue());
		} 
	}
		
	// Helper method: to generate the list of trades
	public static Trade[] GenerateData(IRIS irisNative, Integer objectCount) {
		Trade[] data = new Trade[objectCount];
    	try{
           // Loop through list of trade to generate data for each trade
    	   for (int i=0;i<objectCount;i++) 
		   {
    		   Date tempDate = Date.valueOf("2018-01-01");
    		   Double tempAmount = irisNative.classMethodDouble("%PopulateUtils", "Currency");
    		   String tempName = irisNative.classMethodString("%PopulateUtils", "String") + irisNative.classMethodString("%PopulateUtils", "String") + irisNative.classMethodString("%PopulateUtils", "String");
    		   String tempTrader = irisNative.classMethodString("%PopulateUtils", "Name");
    		   int tempShares = new Random().nextInt(20)+1;
    		   data[i] = new Trade(tempName,tempDate,BigDecimal.valueOf(tempAmount),tempShares,tempTrader);
    		   System.out.println("New trade: " + tempName + " , " + tempDate + " , " + BigDecimal.valueOf(tempAmount) + " , " + tempShares + " , " + tempTrader);
    	   }
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		return data;
	}

	// Helper method: Get connection details from config file
	public static HashMap<String, String> getConfig(String filename) throws FileNotFoundException, IOException{
        // Initial empty map to store connection details
        HashMap<String, String> map = new HashMap<String, String>();

        String line;

        // Using Buffered Reader to read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(nativeplaystocksTask4.class.getResourceAsStream(filename)));

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
