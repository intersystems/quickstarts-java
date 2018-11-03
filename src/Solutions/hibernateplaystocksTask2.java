package Solutions;



import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.logging.Level;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

import Solutions.Demo.Person;
import Solutions.Demo.Trade2;

public class hibernateplaystocksTask2 {
	protected SessionFactory sessionFactory;

	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		hibernateplaystocksTask2 driver = new hibernateplaystocksTask2();
        driver.setup();
        System.out.println("Connected to InterSystems IRIS.");
        
      //Starting interactive prompt
		boolean active = true;
		Scanner scanner = new Scanner(System.in);
		while (active) {
			System.out.println("1. Make a trade (and save)");
			System.out.println("2. Delete all rows");
			System.out.println("3. Display trader by ID");
			System.out.println("4. Display trades by trader last name");
			System.out.println("5. Display leaderboard");
			System.out.println("6. Quit");
			System.out.print("What would you like to do? ");
			
			String option = scanner.next();
			switch (option) {
			case "1":
				System.out.print("Stock name: ");
				String stockName = scanner.next();
				
				System.out.print("Date (YYYY-MM-DD): ");
				Date tempDate = Date.valueOf(scanner.next());
									
				System.out.print("Price: ");
				BigDecimal price = scanner.nextBigDecimal();
				
				System.out.print("Number of Shares: ");
				int shares = scanner.nextInt();
				
				System.out.println("Choose one:");
				System.out.println("1. Existing trader");
				System.out.println("2. New trader");
				String newOrExisting = scanner.next();
				if (newOrExisting.equals("1"))
				{
					System.out.print("Link trade to trader with which ID? ");
					Long traderID = scanner.nextLong();
					driver.create(stockName, tempDate, price, shares, traderID);
				}
				else if (newOrExisting.equals("2"))
				{
					System.out.print("Trader first name: ");
					String traderFirstName = scanner.next();
				
					System.out.print("Trader last name: ");
					String traderLastName = scanner.next();
					
					System.out.print("Trader phone: ");
					String phone = scanner.next();
					
					driver.create(stockName,tempDate,price,shares,traderFirstName,traderLastName,phone);
				}
				else
				{
					System.out.println("Invalid option. Try again");
				}
				break;
			case "2":
				System.out.println("TO DO: Delete traders and trades.");
				break;
			case "3":
				System.out.println("TO DO: Find trader with trades by ID.");
				break;
			case "4":
				System.out.println("TO DO: Find trader by last name.");
				break;
			case "5":
				System.out.println("TO DO: Displaying leaderboard.");
				break;
			case "6":
				System.out.println("Exited.");
				active = false;
				break;				
			default: 
				System.out.println("Invalid option. Try again!");
				break;
			}
		}
        scanner.close();
        driver.exit();
	}
	
    protected void setup() {
    	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
    	        .configure() // configures settings from hibernate.cfg.xml
    	        .build();
    	try {
    	    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    	} 
    	catch (Exception ex) {
    		System.out.println(ex.toString());
    	    StandardServiceRegistryBuilder.destroy(registry);
    	}
    }

    protected void create(String stockName,Date tempDate,BigDecimal price,int shares,String traderFirstName,String traderLastName, String phone) {
    	try {
    		Trade2 trade = new Trade2(stockName, tempDate, price, shares);	
    		System.out.println("Trade created");
    		
    		Person trader = new Person(traderFirstName,traderLastName,phone);
    		System.out.println("person created");
    		
    		trader.getTrades().add(trade);
    		    		
    		System.out.println("Trader " + trader.getFirstname() + " set with references to trade: " + trade.getStockName());
    	   			
    	    Session session = sessionFactory.openSession();
    	    
    	    session.beginTransaction();
    	    System.out.println("transaction started");
    	    
    	    session.save(trade);
    	    System.out.println(trade.getStockName() + " saved with trade ID " + trade.getId());
    	    
    	    session.save(trader);
    	    System.out.println("Trader ID: " + trader.getId() + " saved.");
    	       	
    	    session.getTransaction().commit();
    		
    	    session.close();
        
        }
        catch (Exception e){
        	System.out.println("Error in creation: " + e.getMessage());
        }
    }
    protected void create(String stockName,Date tempDate,BigDecimal price,int shares, Long traderID) {
    	try {
    		Trade2 trade = new Trade2(stockName, tempDate, price, shares);	
    		System.out.println("Trade created");
    		
    		Session session = sessionFactory.openSession();
    		Person trader = session.get(Person.class, traderID);
    		System.out.println("person opened");
    		
    		trader.getTrades().add(trade);
    		trade.setTrader(trader);
    		 
    		System.out.println("Trader " + trader.getFirstname() + " set with references to trade: " + trade.getStockName());
    	   			    	    
    	    session.beginTransaction();
    	    System.out.println("transaction started");
    	    
    	    session.save(trade);
    	    System.out.println(trade.getStockName() + " saved with trade ID " + trade.getId());
    	    
    	    
    	    session.save(trader);
    	    System.out.println("Trader ID: " + trader.getId() + " saved.");
    	       	
    	    session.getTransaction().commit();
    		
    	    session.close();
        
        }
        catch (Exception e){
        	System.out.println("Error in creation: " + e.getMessage());
        }
    }
	
	protected void exit() {
			sessionFactory.close(); 
	}

}
