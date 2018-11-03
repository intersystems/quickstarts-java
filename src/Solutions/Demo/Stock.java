package Solutions.Demo;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="Demo.Stock")
public class Stock {
	private long id;
	private Date tDate;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private int volume;
	private String stockName;

	//Constructors
	public Stock(){}
	public Stock(Date tDate, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, int volume, String stockName) {
		super();
		this.tDate = tDate;
		this.open = open;
		this.high = high;
		this.close = close;
		this.volume = volume;
		this.stockName = stockName;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="TransDate")
	public Date gettDate() {
		return tDate;
	}
	public void settDate(Date tDate) {
		this.tDate = tDate;
	}
	
	@Column(name="StockOpen")
	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	@Column(name="StockClose")
	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Column (name="Name")
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@Override
	public String toString() {
		return "Stock [date=" + tDate + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close
				+ ", volume=" + volume + ", name=" + stockName + "]";
	}
	
}

