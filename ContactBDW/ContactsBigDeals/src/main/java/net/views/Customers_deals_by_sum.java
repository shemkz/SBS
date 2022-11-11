package net.views;

import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Immutable
public class Customers_deals_by_sum {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
	private int id;

    @Column(name = "type_name", updatable = false)
    private String type_name;

    @Column(name = "first_name", updatable = false)
    private String first_name;
    
    @Column(name = "middle_name", updatable = false)
    private String middle_name;
    
    @Column(name = "last_name", updatable = false)
    private String last_name;
    
    @Column(name = "dataset", updatable = false)
	private int dataset;
    
    @Column(name = "sum", updatable = false)
	private long sum;
    
    public Customers_deals_by_sum() {

    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public int getDataset() {
		return dataset;
	}

	public void setDataset(int dataset) {
		this.dataset = dataset;
	}

	public long getSum() {
		return sum;
	}

	public void setSum(long sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "Customers_deals_by_sum [id=" + id + ", type_name=" + type_name + ", first_name=" + first_name
				+ ", middle_name=" + middle_name + ", last_name=" + last_name + ", dataset=" + dataset + ", sum=" + sum
				+ "]";
	}

    
}
