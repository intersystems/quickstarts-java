/*
* PURPOSE: Make connection to InterSystems IRIS using XEP
*/

package Solutions;

import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.intersystems.xep.*;

public class xepplaystocksTask1 {
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
			System.out.println("Connected to InterSystems IRIS.");

	        xepPersister.deleteExtent("Solutions.Demo.Trade");   // Remove old test data
	        xepPersister.importSchema("Solutions.Demo.Trade");   // Import flat schema
	       
	        // Create Event
	        Event xepEvent = xepPersister.getEvent("Solutions.Demo.Trade");
	
	        xepEvent.close();
	        xepPersister.close();
		} catch (XEPException e) { 
			System.out.println("Interactive prompt failed:\n" + e); 
		}
	   } // end main()

    // Helper method: Get connection details from config file
	public static HashMap<String, String> getConfig(String filename) throws FileNotFoundException, IOException{
        // Initial empty map to store connection details
        HashMap<String, String> map = new HashMap<String, String>();

        String line;

        // Using Buffered Reader to read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(xepplaystocksTask1.class.getResourceAsStream(filename)));

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