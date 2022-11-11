package net.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "Types")
public class Types {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type_name")
    private String type_name;

    @Column(name = "description")
    private String description;
    
    @Column(name = "date_create")
    private LocalDate date_create;
    //5
    @Column(name = "dataset")
	private int dataset;
    
    @Column(name = "last_user")
	private String last_user;
    
    @Column(name = "last_changed")
	private LocalDateTime last_changed;
    
    public Types() {

    }

    public Types(int id, String type_name, String description, String last_user) {
    	this.id = id;
    	this.type_name = type_name;
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
		return Objects.hash(dataset, date_create, description, id, last_changed, last_user, type_name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Types other = (Types) obj;
		return dataset == other.dataset && Objects.equals(date_create, other.date_create)
				&& Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(last_changed, other.last_changed) && Objects.equals(last_user, other.last_user)
				&& Objects.equals(type_name, other.type_name);
	}

	@Override
	public String toString() {
		return "Types [id=" + id + ", type_name=" + type_name + ", description=" + description + ", date_create="
				+ date_create + ", dataset=" + dataset + ", last_user=" + last_user + ", last_changed=" + last_changed
				+ "]";
	}

	
    
}