/*
* PURPOSE: This code shows how to use relational (JDBC), object (XEP), and native access side-by-side,
* specifically to store stock company information.
*
* This last task adds functionality to populate test values by calling existing populate methods within InterSystems IRIS using the Native API.
*   - JDBC is used to quickly retrieve all distinct stock names from the Demo.Stock table.
*   - Native API is used to call population methods within InterSystems IRIS for founder and mission statement.
*   - XEP is used to store these objects directly to the database, avoiding any translation back to tables.
*/

package Solutions;

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

import Solutions.Demo.StockInfo;

import com.intersystems.jdbc.IRISConnection;

public class multiplayTask4 {

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
		String host = map.get("host");
		int port = Integer.parseInt(map.get("port"));
		String namespace = map.get("namespace");
		String username = map.get("username");
		String password = map.get("password");
		
		try {
			// Connect to database using EventPersister, which is based on IRISDataSource
	        EventPersister xepPersister = PersisterFactory.createPersister();

	        // Connecting to database
	        xepPersister.connect(host,port,namespace,username,password);
	        System.out.println("Connected to InterSystems IRIS via JDBC.");

	        xepPersister.deleteExtent("Solutions.Demo.StockInfo");   // Remove old test data
	        xepPersister.importSchema("Solutions.Demo.StockInfo");   // Import flat schema
	       
	        //***Initializations***
	        // Create XEP Event for object access
	        Event xepEvent = xepPersister.getEvent("Solutions.Demo.StockInfo");

	        // Create JDBC statement object for SQL and IRIS Native access
	        Statement myStatement = xepPersister.createStatement();
	        
	        // Create IRIS Native object
	        IRIS irisNative = IRIS.createIRIS((IRISConnection)xepPersister);
	        
	        
	        //***Running code***
	        System.out.println("Generating stock info table...");
			
			// Get stock names (JDBC)
			ResultSet myRS = myStatement.executeQuery("SELECT distinct name FROM demo.stock");
			
												
			// Create java objects and store to database (XEP)
			ArrayList<StockInfo> stocksList = new ArrayList<StockInfo>();
			while(myRS.next())
			{
				StockInfo stock = new StockInfo();
				stock.name = myRS.getString("name");
				System.out.println("created stockinfo array.");
				
				//generate mission and founder names (Native API)
				stock.founder = irisNative.classMethodString("%PopulateUtils", "Name");
				stock.mission = irisNative.classMethodString("%PopulateUtils", "Mission");
				
				System.out.println("Adding object with name " + stock.name + " founder " + stock.founder + " and mission " + stock.mission);
				stocksList.add(stock);
			}
			StockInfo[] stocksArray = stocksList.toArray(new StockInfo[stocksList.size()]);
			
			xepEvent.store(stocksArray);
					
			// Close everything
		    xepEvent.close();
		    xepPersister.close();
						
		} catch (SQLException e) {
			 System.out.println("Error creating stock listing: " + e.getMessage());
		}
	        
	}

	// Helper method: Get connection details from config file
	public static HashMap<String, String> getConfig(String filename) throws FileNotFoundException, IOException{
        // Initial empty map to store connection details
        HashMap<String, String> map = new HashMap<String, String>();

        String line;

        // Using Buffered Reader to read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(multiplayTask1.class.getResourceAsStream(filename)));

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