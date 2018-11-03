package Solutions;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Scanner;

import com.intersystems.xep.*;

import Solutions.Demo.Trade;

public class xepplaystocksTask2 {
	  public static void main(String[] args) throws Exception {
	    try {
			String user = "SuperUser";
			String pass = "SYS";
	    	
	    	// Initialize sampleArray to hold Trade items
	    	Trade[] sampleArray = null;
	    	
	    	// Connect to database using EventPersister
	        EventPersister xepPersister = PersisterFactory.createPersister();
	        xepPersister.connect("127.0.0.1",51773,"USER",user,pass);
			System.out.println("Connected to InterSystems IRIS.");
	        xepPersister.deleteExtent("Solutions.Demo.Trade");   // remove old test data
	        xepPersister.importSchema("Solutions.Demo.Trade");   // import flat schema
	       
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
					System.out.println("Saving trades.");
					XEPSaveTrades(sampleArray, xepEvent);
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
	  
	public static Trade[] CreateTrade(String stockName, Date tDate, BigDecimal price, int shares, String trader, Trade[] sampleArray)
	{
		Trade sampleObject = new Trade(stockName,tDate,price,shares,trader); //
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
	public static Long XEPSaveTrades(Trade[] sampleArray,Event xepEvent)
	{
		Long startTime = System.currentTimeMillis(); //To calculate execution time
		xepEvent.store(sampleArray);
		Long totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Saved " + sampleArray.length + " trade(s).");
		return totalTime;
	}

} 