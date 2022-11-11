package net.tables;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "Brands", 
uniqueConstraints= {// @UniqueConstraint(columnNames = {"type_name", "TIN"}),
					//@UniqueConstraint(columnNames = {"dataset", "type_name", "mobile_p"}),
					@UniqueConstraint(columnNames = {"dataset", "brand_name"}) })
public class Brands {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "brand_name")
    private String brand_name;
    //10
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

    public Brands() {

    }

    public Brands(int id, String brand_name, String description, String last_user) {
    	this.id = id;
    	this.brand_name = brand_name;
        this.description = description;
	    this.last_user = last_user;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
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
		return Objects.hash(brand_name, dataset, date_create, description, id, last_changed, last_user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Brands other = (Brands) obj;
		return Objects.equals(brand_name, other.brand_name) && dataset == other.dataset
				&& Objects.equals(date_create, other.date_create) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(last_changed, other.last_changed)
				&& Objects.equals(last_user, other.last_user);
	}

	@Override
	public String toString() {
		return "Brands [id=" + id + ", brand_name=" + brand_name + ", description=" + description + ", date_create="
				+ date_create + ", dataset=" + dataset + ", last_user=" + last_user + ", last_changed=" + last_changed
				+ "]";
	}


}