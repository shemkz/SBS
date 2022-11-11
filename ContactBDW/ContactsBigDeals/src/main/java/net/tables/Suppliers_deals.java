package net.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Suppliers_deals", 
		uniqueConstraints=@UniqueConstraint(columnNames = {"id_supplier","name_good","price","quantity","sum","date_deal"}))
public class Suppliers_deals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    //@ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="id_supplier")
    //private Suppliers id_supplier;
    @Column(name = "id_supplier")
    private int id_supplier;

    @Column(name = "name_good")
    private String name_good;
    
    @Column(name = "price")
    private Long price;
    
    @Column(name = "quantity")
    private int quantity;
    //5
    @Column(name = "sum")
    private Long sum;
    
    @Column(name = "details")
    private String details;
    
    @Column(name = "date_deal")
    private LocalDate date_deal;
    
    @Column(name = "dataset")
	private int dataset;
    
    @Column(name = "last_user")
	private String last_user;
    
    @Column(name = "last_changed")
	private LocalDateTime last_changed;
    
    public Suppliers_deals() {

    }

    public Suppliers_deals(int id, int id_supplier, String name_good, Long price, int quantity, 
    		Long sum, String details, int dataset, String last_user) {//, Date date_deal
    	this.id = id;
    	this.id_supplier = id_supplier;
        this.name_good = name_good;
        this.price = price;
        this.quantity = quantity;
        
        this.sum = sum;
        this.details = details;
        this.dataset = dataset;
        this.last_user = last_user;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_supplier() {
		return id_supplier;
	}

	public void setId_supplier(int id_supplier) {
		this.id_supplier = id_supplier;
	}

	public String getName_good() {
		return name_good;
	}

	public void setName_good(String name_good) {
		this.name_good = name_good;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDate getDate_deal() {
		return date_deal;
	}

	public void setDate_deal(LocalDate date_deal) {
		this.date_deal = date_deal;
	}

	public int getDataset() {
		return dataset;
	}

	public void setDataset(int dataset) {
		this.dataset = dataset;
	}

	public String getLast_user() {
		return last_user;
	}

	public void setLast_user(String last_user) {
		this.last_user = last_user;
	}

	public LocalDateTime getLast_changed() {
		return last_changed;
	}

	public void setLast_changed(LocalDateTime last_changed) {
		this.last_changed = last_changed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataset, date_deal, details, id, id_supplier, last_changed, last_user, name_good, price,
				quantity, sum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Suppliers_deals other = (Suppliers_deals) obj;
		return dataset == other.dataset && Objects.equals(date_deal, other.date_deal)
				&& Objects.equals(details, other.details) && id == other.id && id_supplier == other.id_supplier
				&& Objects.equals(last_changed, other.last_changed) && Objects.equals(last_user, other.last_user)
				&& Objects.equals(name_good, other.name_good) && Objects.equals(price, other.price)
				&& quantity == other.quantity && Objects.equals(sum, other.sum);
	}

	@Override
	public String toString() {
		return "Suppliers_deals [id=" + id + ", id_supplier=" + id_supplier + ", name_good=" + name_good + ", price="
				+ price + ", quantity=" + quantity + ", sum=" + sum + ", details=" + details + ", date_deal="
				+ date_deal + ", dataset=" + dataset + ", last_user=" + last_user + ", last_changed=" + last_changed
				+ "]";
	}


}