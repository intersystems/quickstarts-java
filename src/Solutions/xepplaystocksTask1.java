package Solutions;

import com.intersystems.xep.*;

public class xepplaystocksTask1 {
	  public static void main(String[] args) throws Exception {
	    try {
			String user = "SuperUser";
			String pass = "SYS";
	    	
	    	// Connect to database using EventPersister
	        EventPersister xepPersister = PersisterFactory.createPersister();
	        xepPersister.connect("127.0.0.1",51773,"USER",user,pass);
			System.out.println("Connected to InterSystems IRIS.");
	        xepPersister.deleteExtent("Solutions.Demo.Trade");   // remove old test data
	        xepPersister.importSchema("Solutions.Demo.Trade");   // import flat schema
	       
	        // Create Event
	        Event xepEvent = xepPersister.getEvent("Solutions.Demo.Trade");
	
	        xepEvent.close();
	        xepPersister.close();
		} catch (XEPException e) { 
			System.out.println("Interactive prompt failed:\n" + e); 
		}
	   } // end main()
	  
} 