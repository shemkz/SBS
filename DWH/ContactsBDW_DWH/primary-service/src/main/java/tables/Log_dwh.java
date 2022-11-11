package tables;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "Log_dwh", 
uniqueConstraints= {// @UniqueConstraint(columnNames = {"type_name", "TIN"}),
					//@UniqueConstraint(columnNames = {"dataset", "type_name", "mobile_p"}),
					@UniqueConstraint(columnNames = {"dataset", "actions", "last_changed"}) })
public class Log_dwh {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "actions")
    private String actions;
    
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

    public Log_dwh() {

    }

    public Log_dwh(int id, String actions, String description,  
    		String last_user) {//LocalDate date_create,int dataset, , LocalDateTime last_changed
    	this.id = id;
    	this.actions = actions;
        this.description = description;
	    this.last_user = last_user;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
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
		return Objects.hash(actions, dataset, date_create, description, id, last_changed, last_user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Log_dwh other = (Log_dwh) obj;
		return Objects.equals(actions, other.actions) && dataset == other.dataset
				&& Objects.equals(date_create, other.date_create) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(last_changed, other.last_changed)
				&& Objects.equals(last_user, other.last_user);
	}

	@Override
	public String toString() {
		return "Log_dwh [id=" + id + ", actions=" + actions + ", description=" + description + ", date_create="
				+ date_create + ", dataset=" + dataset + ", last_user=" + last_user + ", last_changed=" + last_changed
				+ "]";
	}
    
    
}
