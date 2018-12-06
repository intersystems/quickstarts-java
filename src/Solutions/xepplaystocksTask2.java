/*
 * PURPOSE: Create some simple trades and save it to database
 *
 * NOTES: When running the application:
 * 1. Choose option 1 to generate new trade
 * 2. Choose option 2 to save trades
 */

package Solutions;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Scanner;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.intersystems.xep.*;

import Solutions.Demo.Trade;

public class xepplaystocksTask2 {
	public static void main(String[] args) throws Exception {
		// Initialize map to store connection details from config.txt
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			map = getConfig("config.txt");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}

		// Retrieve connection information from configuration file
		String host = map.get("host");
		int port = Integer.parseInt(map.get("port"));
		String namespace = map.get("namespace");
		String username = map.get("username");
		String password = map.get("password");  

		try {	    	
			// Initialize sampleArray to hold Trade items
			Trade[] sampleArray = null;

			// Connect to database using EventPersister, which is based on IRISDataSource
			EventPersister xepPersister = PersisterFactory.createPersister();

			// Connecting to database
			xepPersister.connect(host,port,namespace,username,password);
			System.out.println("Connected to InterSystems IRIS.");

			xepPersister.deleteExtent("Solutions.Demo.Trade");   // Remove old test data
			xepPersister.importSchema("Solutions.Demo.Trade");   // Import flat schema

			// Create Event
			Event xepEvent = xepPersister.getEvent("Solutions.Demo.Trade");


			// Starting interactive prompt
			boolean always = true;
			Scanner scanner = new Scanner(System.in);
			while (always) {
				System.out.println("1. Make a trade (do not save)");
				System.out.println("2. Confirm all trades");
				System.out.println("3. Generate and save multiple trades");
				System.out.println("4. Retrieve all trades; show execution statistics");
				System.out.println("5. JDBC Comparison - Create and save multiple trades");
				System.out.println("6. Quit");
				System.out.print("What would you like to do? ");

				String option = scanner.next();
				switch (option) {
				case "1":
					//Create trade object
					System.out.print("Stock name: ");
					String name = scanner.next();

					System.out.print("Date (YYYY-MM-DD): ");
					Date tempDate = Date.valueOf(scanner.next());

					System.out.print("Price: ");
					BigDecimal price = scanner.nextBigDecimal();

					System.out.print("Number of Shares: ");
					int shares = scanner.nextInt();

					System.out.print("Trader name: ");
					String traderName = scanner.next();

					sampleArray = CreateTrade(name,tempDate,price,shares,traderName,sampleArray);
					break;
				case "2":
					//Save trades
					System.out.println("Saving trades...");
		            if(sampleArray != null){
		                XEPSaveTrades(sampleArray, xepEvent);
		            }
		            else{
		            	System.out.println("There is no new trade to save");
		            }
					sampleArray = null;
					break;
				case "3":
					System.out.println("TO DO: Generate and save multiple trades");	
					break;
				case "4":
					System.out.println("TO DO: Retrieve all trades");
					break;
				case "5":
					System.out.println("TO DO: JDBC Comparison - Create and save multiple trades");
					break;
				case "6":
					System.out.println("Exited.");
					always = false;
					break;
				default: 
					System.out.println("Invalid option. Try again!");
					break;
				}

			}
			scanner.close();	
			xepEvent.close();
			xepPersister.close();
		} catch (XEPException e) { 
			System.out.println("Interactive prompt failed:\n" + e); 
		}
	} // end main()

	// Create sample and add it to the array
	public static Trade[] CreateTrade(String stockName, Date tDate, BigDecimal price, int shares, String trader, Trade[] sampleArray)
	{
		Trade sampleObject = new Trade(stockName,tDate,price,shares,trader);
		System.out.println("New Trade: " + shares + " shares of " + stockName + " purchased on date " + tDate.toString() + " at price " + price + " by " + trader + ".");

		int currentSize = 0;
		int newSize = 1;
		if (sampleArray != null)
		{
			currentSize = sampleArray.length;
			newSize = currentSize + 1;
		} 

		Trade[] newArray = new Trade[ newSize ];
		for (int i=0; i < currentSize; i++)
		{
			newArray[i] = sampleArray[i];
		}
		newArray[newSize- 1] = sampleObject;
		System.out.println("Added " + stockName + " to the array. Contains " + newSize + " trade(s).");
		return newArray;
	}

	// Save array of trade into database using xepEvent
	public static Long XEPSaveTrades(Trade[] sampleArray,Event xepEvent)
	{
		Long startTime = System.currentTimeMillis(); // To calculate execution time
		xepEvent.store(sampleArray);
		Long totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Saved " + sampleArray.length + " trade(s).");
		return totalTime;
	}

	// Helper method: Get connection details from config file
	public static HashMap<String, String> getConfig(String filename) throws FileNotFoundException, IOException{
		// Initial empty map to store connection details
		HashMap<String, String> map = new HashMap<String, String>();

		String line;

		// Using Buffered Reader to read file
		BufferedReader reader = new BufferedReader(new InputStreamReader(xepplaystocksTask2.class.getResourceAsStream(filename)));

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