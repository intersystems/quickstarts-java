package Solutions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.intersystems.jdbc.IRIS;
import com.intersystems.xep.Event;
import com.intersystems.xep.EventPersister;
import com.intersystems.xep.PersisterFactory;

import Solutions.Demo.StockInfo;

import com.intersystems.jdbc.IRISConnection;

public class multiplayTask4 {

	public static void main(String[] args) {
		String user = "SuperUser";
		String pass = "SYS";
		
		try {
			// Connect to database using EventPersister, which is based on IRISDataSource
	        EventPersister xepPersister = PersisterFactory.createPersister();
	        xepPersister.connect("127.0.0.1",51773,"User",user,pass); 
	        System.out.println("Connected to InterSystems IRIS via JDBC.");
	        xepPersister.deleteExtent("Solutions.Demo.StockInfo");   // remove old test data
	        xepPersister.importSchema("Solutions.Demo.StockInfo");   // import flat schema
	       
	        //***Initializations***
	        //Create XEP Event for object access
	        Event xepEvent = xepPersister.getEvent("Solutions.Demo.StockInfo");

	        //Create JDBC statement object for SQL and IRIS Native access
	        Statement myStatement = xepPersister.createStatement();
	        
	        //Create IRIS Native object
	        IRIS irisNative = IRIS.createIRIS((IRISConnection)xepPersister);
	        
	        
	        //***Running code***
	        System.out.println("Generating stock info table...");
			
			//Get stock names (JDBC)
			ResultSet myRS = myStatement.executeQuery("SELECT distinct name FROM demo.stock");
			
												
			//Create java objects and store to database (XEP)
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
					
			//Close everything
		    xepEvent.close();
		    xepPersister.close();
						
		} catch (SQLException e) {
			 System.out.println("Error creating stock listing: " + e.getMessage());
		}
	        
	}
}