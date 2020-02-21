/*
* PURPOSE: This code shows how to use relational (JDBC), object (XEP), and native access side-by-side,
* specifically to store stock company information.
*
* This last task adds functionality to populate test values by calling existing populate methods within InterSystems IRIS using the Native API.
*   - JDBC is used to quickly retrieve all distinct stock names from the Demo.Stock table.
*   - Native API is used to call population methods within InterSystems IRIS for founder and mission statement.
*   - XEP is used to store these objects directly to the database, avoiding any translation back to tables.
*/

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import com.intersystems.jdbc.IRIS;
import com.intersystems.xep.Event;
import com.intersystems.xep.EventPersister;
import com.intersystems.xep.PersisterFactory;

import Demo.StockInfo;

import com.intersystems.jdbc.IRISConnection;

public class multimodelplaystocksTask4 {

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
		String ip = map.get("ip");
		int port = Integer.parseInt(map.get("port"));
		String namespace = map.get("namespace");
		String username = map.get("username");
		String password = map.get("password");
		
		try {
			// Connect to database using EventPersister, which is based on IRISDataSource
	        EventPersister xepPersister = PersisterFactory.createPersister();

	        // Connecting to database
	        xepPersister.connect(ip, port, namespace, username, password);
	        System.out.println("Connected to InterSystems IRIS via JDBC.");

	        xepPersister.deleteExtent("Demo.StockInfo");   // Remove old test data
	        xepPersister.importSchema("Demo.StockInfo");   // Import flat schema
	       
	        // Create XEP Event for object access
	        Event xepEvent = xepPersister.getEvent("Demo.StockInfo");

	        // Create JDBC statement object for SQL and IRIS Native access
	        Statement myStatement = xepPersister.createStatement();
	        
	        // Create IRIS Native object
	        IRIS irisNative = IRIS.createIRIS((IRISConnection)xepPersister);
	        
			boolean always = true;
			Scanner scanner = new Scanner(System.in);
			while (always) {
				System.out.println("1. Retrieve all stock names using ADO.NET");
				System.out.println("2. Generate sample founders and mission statements using XEP");
				System.out.println("3. Populate values for founders and mission statements using Native API");
				System.out.println("4. Quit");
				System.out.print("What would you like to do? ");

				String option = scanner.next();
				switch (option) {
				case "1":
					retrieveStock(myStatement);
					break;
				case "2":
					generateSampleMissions(myStatement, xepEvent);
					break;
				case "3":
					populateMissions(myStatement, xepEvent);
					break;
				case "4":
					System.out.println("Exited.");
					always = false;
					break;
				default: 
					System.out.println("Invalid option. Try again!");
					break;
				}
			}	
			// Close everything
		    xepEvent.close();
		    xepPersister.close();
						
		} catch (SQLException e) {
			 System.out.println("Error creating stock listing: " + e.getMessage());
		}
	        
	}

	// Query all stock names using ADO.NET
	public static void retrieveStock(Statement myStatement){
		System.out.println("Generating stock info table...");
			
		// Get stock names (JDBC)
		ResultSet myRS = myStatement.executeQuery("SELECT distinct name FROM demo.stock");
					
		while(myRS.next())
		{
			System.out.println(myRS.getString("name"));		
		}
	}

	// Generate and store sample founder and mission statement using XEP
	public static void generateSampleMissions(Statement myStatement, Event xepEvent){
		// Get stock names (JDBC)
		ResultSet myRS = myStatement.executeQuery("SELECT distinct name FROM demo.stock");
											
		// Create java objects and store to database (XEP)
		ArrayList<StockInfo> stocksList = new ArrayList<StockInfo>();
		while(myRS.next())
		{
			StockInfo stock = new StockInfo();
			stock.name = myRS.getString("name");
			System.out.println("Created stockinfo array.");
			
			// Generate mission and founder names
			stock.founder = "test founder";
			stock.mission = "some mission statement";
			
			System.out.println("Adding object with name " + stock.name + " founder " + stock.founder + " and mission " + stock.mission);
			stocksList.add(stock);
		}
		StockInfo[] stocksArray = stocksList.toArray(new StockInfo[stocksList.size()]);
		
		xepEvent.store(stocksArray);
	}

	// Use Native API call population methods within InterSystems IRIS to populate values for founder and mission statement
	public static void populateMissions(Statement myStatement, Event xepEvent){
		// Get stock names (JDBC)
		ResultSet myRS = myStatement.executeQuery("SELECT distinct name FROM demo.stock");
		
											
		// Create java objects and store to database (XEP)
		ArrayList<StockInfo> stocksList = new ArrayList<StockInfo>();
		while(myRS.next())
		{
			StockInfo stock = new StockInfo();
			stock.name = myRS.getString("name");
			System.out.println("created stockinfo array.");
			
			// Generate mission and founder names (Native API)
			stock.founder = irisNative.classMethodString("%PopulateUtils", "Name");
			stock.mission = irisNative.classMethodString("%PopulateUtils", "Mission");
			
			System.out.println("Adding object with name " + stock.name + " founder " + stock.founder + " and mission " + stock.mission);
			stocksList.add(stock);
		}
		StockInfo[] stocksArray = stocksList.toArray(new StockInfo[stocksList.size()]);
		
		xepEvent.store(stocksArray);
	}	

	// Helper method: Get connection details from config file
	public static HashMap<String, String> getConfig(String filename) throws FileNotFoundException, IOException{
        // Initial empty map to store connection details
        HashMap<String, String> map = new HashMap<String, String>();

        String line;

        // Using Buffered Reader to read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(multimodelplaystocksTask4.class.getResourceAsStream(filename)));

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