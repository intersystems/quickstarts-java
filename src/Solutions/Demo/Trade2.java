package Solutions.Demo;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.*;

import Solutions.Demo.Person;

@Entity
@Table (name = "hibernate.trade")
public class Trade2 {
	private long id;
	private String stockName;
	private Date purchaseDate;
	private BigDecimal purchasePrice;
	private int shares;
	private Person trader;
	
	//Constructors
	public Trade2(){}
	
	public Trade2 (String stockName, Date purchaseDate, BigDecimal purchasePrice, int shares) { //, Date transDate
		this.stockName = stockName;
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
		this.shares = shares;
	}
	
	//Getters and Setters
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	@ManyToOne
    @JoinColumn(name = "trader")
	public Person getTrader() {
		return trader;
	}

	public void setTrader(Person trader) {
		this.trader = trader;
	} 

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

}
