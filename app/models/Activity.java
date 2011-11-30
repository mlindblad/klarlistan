package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class Activity extends Model {

	public String name;
	public String location;
	public Date date;
	public String information;
	
	@ManyToMany(mappedBy="followedEvents") 
    public Set<User> followsByUsers = new HashSet<User>(); 
	
	public Activity(String name, String location, Date date, String information) {
		this.name = name;
		this.location = location;
		this.date = date;
		this.information = information;
	}
}
