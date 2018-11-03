package Solutions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import com.intersystems.jdbc.IRISDataSource;

public class jdbcplaystocksTask2 {
	
	public static void main(String[] args) {
		String dbUrl = "jdbc:IRIS://127.0.0.1:51773/USER";
		String user = "superuser";
		String pass = "SYS";
		
		try {
			//Making connection
			IRISDataSource ds = new IRISDataSource(); 
			ds.setURL(dbUrl);
			ds.setUser(user);
			ds.setPassword(pass);
			Connection dbconnection = ds.getConnection();
			System.out.println("Connected to InterSystems IRIS via JDBC.");
			
			//Starting interactive prompt
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
					System.out.println("TO DO: Create Portfolio table");
					break;
				case "3":
					System.out.println("TO DO: Add to Portfolio");
					break;
				case "4":
					System.out.println("TO DO: Update Portfolio");
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
	public static void FindTopOnDate(Connection dbconnection, String onDate)
	{
		//Find top 10 stocks on a particular date
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
		
}
	
