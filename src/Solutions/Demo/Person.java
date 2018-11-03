package Solutions.Demo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table (name = "demo.person")
public class Person {
	private long id;
	private String firstname;
	private String lastname;
	private String phone;
	private List<Trade2> trades = new ArrayList<>();
	

	//Constructors
	public Person(){}
	
	public Person(String firstname, String lastname, String phone) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
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
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	@Column(name="Phonenumber")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@JoinColumn (name="trader")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Trade2> getTrades() {
		return trades;
	}

	public void setTrades(List<Trade2> trades) {
		this.trades = trades;
	}

	

}
