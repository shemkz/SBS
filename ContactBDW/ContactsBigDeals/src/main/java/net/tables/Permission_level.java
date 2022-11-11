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
@Table(name = "Permission_level")
public class Permission_level {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "permit_level")
	private String permit_level;
	
	@Column(name = "description")
	private String description;
	
    @Column(name = "date_create")
    private LocalDate date_create;
    
    @Column(name = "dataset")
	private int dataset;
    //5
    @Column(name = "last_user")
	private String last_user;
    
    @Column(name = "last_changed")
	private LocalDateTime last_changed;
	    
	public Permission_level() {

	}

	public Permission_level(int id, String permit_level, String description, LocalDate date_create, 
				int dataset, String last_user) {
		this.id = id;
	    this.permit_level = permit_level;
	    this.description = description;
	    this.date_create = date_create;
	    this.dataset = dataset;
	    this.last_user = last_user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPermit_level() {
		return permit_level;
	}

	public void setPermit_level(String permit_level) {
		this.permit_level = permit_level;
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
		return Objects.hash(dataset, date_create, description, id, last_changed, last_user, permit_level);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission_level other = (Permission_level) obj;
		return dataset == other.dataset && Objects.equals(date_create, other.date_create)
				&& Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(last_changed, other.last_changed) && Objects.equals(last_user, other.last_user)
				&& Objects.equals(permit_level, other.permit_level);
	}

	@Override
	public String toString() {
		return "Permission_level [id=" + id + ", permit_level=" + permit_level + ", description=" + description
				+ ", date_create=" + date_create + ", dataset=" + dataset + ", last_user=" + last_user
				+ ", last_changed=" + last_changed + "]";
	}

	
}
