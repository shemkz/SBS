package net.tables;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "Customers", 
uniqueConstraints= {// @UniqueConstraint(columnNames = {"type_name", "TIN"}),
					//@UniqueConstraint(columnNames = {"dataset", "type_name", "mobile_p"}),
					@UniqueConstraint(columnNames = {"dataset", "type_name", "first_name", "middle_name", "last_name"}) })
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type_name")
    private String type_name;

    @Column(name = "first_name")
    private String first_name;
    
    @Column(name = "middle_name")
    private String middle_name;
    
    @Column(name = "last_name")
    private String last_name;
    //5
    @Column(name = "TIN")
    private String TIN;
    
    @Column(name = "mobile_p")
    private String mobile_p;

    @Column(name = "station_p")
    private String station_p;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "date_create")
    private LocalDate date_create;
    //10
    @Column(name = "description")
    private String description;
    
    @Column(name = "dataset")
	private int dataset;
    
    @Column(name = "last_user")
	private String last_user;
    
    @Column(name = "last_changed")
	private LocalDateTime last_changed;

    public Customers() {

    }

    public Customers(int id, String type_name, String first_name, String middle_name, String last_name, String TIN, 
    		String mobile_p, String station_p, String address, String description, 
    		String last_user) {//LocalDate date_create,int dataset, , LocalDateTime last_changed
    	this.id = id;
    	this.type_name = type_name;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        
        this.TIN = TIN;
        this.mobile_p = mobile_p;
        this.station_p = station_p;
        this.address = address;
        this.description = description;
        
        //this.date_create = date_create;
	    //this.dataset = dataset;
	    this.last_user = last_user;
	    //this.last_changed = last_changed;
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

	public String getTIN() {
		return TIN;
	}

	public void setTIN(String tIN) {
		TIN = tIN;
	}

	public String getMobile_p() {
		return mobile_p;
	}

	public void setMobile_p(String mobile_p) {
		this.mobile_p = mobile_p;
	}

	public String getStation_p() {
		return station_p;
	}

	public void setStation_p(String station_p) {
		this.station_p = station_p;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDate_create() {
		return date_create;
	}

	public void setDate_create(LocalDate date_create) {
		this.date_create = date_create;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return Objects.hash(TIN, address, dataset, date_create, description, first_name, id, last_changed, last_name,
				last_user, middle_name, mobile_p, station_p, type_name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customers other = (Customers) obj;
		return Objects.equals(TIN, other.TIN) && Objects.equals(address, other.address) && dataset == other.dataset
				&& Objects.equals(date_create, other.date_create) && Objects.equals(description, other.description)
				&& Objects.equals(first_name, other.first_name) && id == other.id
				&& Objects.equals(last_changed, other.last_changed) && Objects.equals(last_name, other.last_name)
				&& Objects.equals(last_user, other.last_user) && Objects.equals(middle_name, other.middle_name)
				&& Objects.equals(mobile_p, other.mobile_p) && Objects.equals(station_p, other.station_p)
				&& Objects.equals(type_name, other.type_name);
	}

	@Override
	public String toString() {
		return "Customers [id=" + id + ", type_name=" + type_name + ", first_name=" + first_name + ", middle_name="
				+ middle_name + ", last_name=" + last_name + ", TIN=" + TIN + ", mobile_p=" + mobile_p + ", station_p="
				+ station_p + ", address=" + address + ", date_create=" + date_create + ", description=" + description
				+ ", dataset=" + dataset + ", last_user=" + last_user + ", last_changed=" + last_changed + "]";
	}

    
}