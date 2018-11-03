package Solutions;

import java.sql.SQLException;
import java.util.Scanner;

import com.intersystems.jdbc.IRISConnection;
import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISDataSource;

public class nativeplaystocksTask1 {

	public static void main(String[] args) {
		String dbUrl = "jdbc:IRIS://127.0.0.1:51773/USER";
		String user = "SuperUser";
		String pass = "SYS";
		
		try {
			//Making connection
			IRISDataSource ds = new IRISDataSource();
			ds.setURL(dbUrl);
			ds.setUser(user);
			ds.setPassword(pass);
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
					System.out.println("TO DO: Stock stock data");
					break;
				case "3":
					System.out.println("TO DO: View stock data");
					break;
				case "4":
					System.out.println("TO DO: Generate trades");
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
		catch ( SQLException e) 
		{ 
			System.out.println("SQL error in application: " + e.getMessage());
		} 
		catch (Exception e) 
		{
			System.out.println("Error - Exception thrown: " + e.getMessage());
		} 
	}
	public static void SetTestGlobal(IRIS irisNative)
	{
		//Write to a test global
		irisNative.set(8888, "^testglobal", "1");
		Integer globalValue = irisNative.getInteger("^testglobal", "1");
		System.out.println("The value of ^testglobal(1) is " + globalValue);
	}
			
}
