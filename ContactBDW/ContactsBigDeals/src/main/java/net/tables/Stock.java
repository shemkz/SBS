package net.tables;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "Stock", 
uniqueConstraints= {// @UniqueConstraint(columnNames = {"type_name", "TIN"}),
					//@UniqueConstraint(columnNames = {"dataset", "type_name", "mobile_p"}),
					@UniqueConstraint(columnNames = {"dataset", "product_name", "measure"}) })
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "product_name")
    private String product_name;

    @Column(name = "measure")
    @Enumerated(EnumType.STRING)
    private measure_type measure;
    
    @Column(name = "price")
    private Long price;
    
    @Column(name = "quantity")
    private Long quantity;
    //5
    @Column(name = "amount")
    private Long amount;

    @Column(name = "description")
    private String description;
   
    @Column(name = "date_create")
    private LocalDate date_create;
   
    @Column(name = "dataset")
	private int dataset;
    
    @Column(name = "last_user")
	private String last_user;
    
    @Column(name = "last_changed")
	private LocalDateTime last_changed;
    
    public enum measure_type {
    	Единица,Комплект,Метр,М_погон,Кг,Коробка,Прочее
    }

    public Stock() {

    }

    public Stock(int id, String product_name, String measure, Long price, Long quantity, Long amount, 
    		String description, String last_user) {//
    	this.id = id;
    	this.product_name = product_name;
        this.measure = measure_type.valueOf(measure);
        this.price = price;
        this.quantity = quantity;
        
        this.amount = amount;
        this.description = description;
	    this.last_user = last_user;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getMeasure() {
		return measure.toString();
	}

	public void setMeasure(String measure) {
		this.measure = measure_type.valueOf(measure);
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate_create() {
		return date_create;
	}

	public void setDate_create(LocalDate date_create) {
		this.date_create = date_create;
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
		return Objects.hash(amount, dataset, date_create, description, id, last_changed, last_user, measure, price,
				product_name, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		return Objects.equals(amount, other.amount) && dataset == other.dataset
				&& Objects.equals(date_create, other.date_create) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(last_changed, other.last_changed)
				&& Objects.equals(last_user, other.last_user) && Objects.equals(measure, other.measure)
				&& Objects.equals(price, other.price) && Objects.equals(product_name, other.product_name)
				&& Objects.equals(quantity, other.quantity);
	}

	@Override
	public String toString() {
		return "Stock [id=" + id + ", product_name=" + product_name + ", measure=" + measure + ", price=" + price
				+ ", quantity=" + quantity + ", amount=" + amount + ", description=" + description + ", date_create="
				+ date_create + ", dataset=" + dataset + ", last_user=" + last_user + ", last_changed=" + last_changed
				+ "]";
	}


}