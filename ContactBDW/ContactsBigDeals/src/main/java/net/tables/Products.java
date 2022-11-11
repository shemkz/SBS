package net.tables;

import javax.persistence.*;

import net.tables.Stock.measure_type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "Products", 
uniqueConstraints= {// @UniqueConstraint(columnNames = {"type_name", "TIN"}),
					//@UniqueConstraint(columnNames = {"dataset", "type_name", "mobile_p"}),
					@UniqueConstraint(columnNames = {"dataset", "type_name", "brand_name", "product_name", "measure"}) })
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type_name")
    private String type_name;

    @Column(name = "brand_name")
    private String brand_name;
    
    @Column(name = "product_name")
    private String product_name;
    
    @Column(name = "measure")
    @Enumerated(EnumType.STRING)
    private measure_type measure;
    //5
    @Column(name = "price")
    private Long price;

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

    public Products() {

    }

    public Products(int id, String type_name, String brand_name, String product_name, String measure, 
    		Long price, String description, String last_user) {//
    	this.id = id;
    	this.type_name = type_name;
        this.brand_name = brand_name;
        this.product_name = product_name;
        this.measure = measure_type.valueOf(measure);
        
        this.price = price;
        this.description = description;
	    this.last_user = last_user;
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

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
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
		return Objects.hash(brand_name, dataset, date_create, description, id, last_changed, last_user, measure, price,
				product_name, type_name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Products other = (Products) obj;
		return Objects.equals(brand_name, other.brand_name) && dataset == other.dataset
				&& Objects.equals(date_create, other.date_create) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(last_changed, other.last_changed)
				&& Objects.equals(last_user, other.last_user) && Objects.equals(measure, other.measure)
				&& Objects.equals(price, other.price) && Objects.equals(product_name, other.product_name)
				&& Objects.equals(type_name, other.type_name);
	}

	@Override
	public String toString() {
		return "Products [id=" + id + ", type_name=" + type_name + ", brand_name=" + brand_name + ", product_name="
				+ product_name + ", measure=" + measure + ", price=" + price + ", description=" + description
				+ ", date_create=" + date_create + ", dataset=" + dataset + ", last_user=" + last_user
				+ ", last_changed=" + last_changed + "]";
	}


    
}