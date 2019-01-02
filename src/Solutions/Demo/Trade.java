/*
* PURPOSE: This class is being used by XEP and Native API classes to generate list of trades
* This generates some trades with default values
*/

package Solutions.Demo;

import java.math.BigDecimal;
import java.sql.Date;

public class Trade {
	public String stockName;
	public Date purchaseDate;
	public BigDecimal purchasePrice;
	public int shares;
	public String traderName;

	// Constructors
	public Trade(){}
	
	public Trade(String stockName, Date purchaseDate, BigDecimal purchasePrice, int shares, String traderName) { 
		this.stockName = stockName;
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
		this.shares = shares;
		this.traderName = traderName;
	}
	
    public static Trade[] generateSampleData(int objectCount) {
    	Trade[] data = new Trade[objectCount];
    	try{
    	   
    	   for (int i=0;i<objectCount;i++) {
    		   Date tempDate = Date.valueOf("2018-01-01");
    		   BigDecimal tempPrice = BigDecimal.valueOf(25.00);
    		   
    		   data[i] = new Trade("XXX",tempDate,tempPrice,5,"TestTrader"); 
    	   }
        
       }
       catch (Exception e) {
    	   System.out.println(e.getMessage());
       }
    	return data;
     }
}
