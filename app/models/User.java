package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	public String name;
	public String email;
	public String mobileNr;
	public String password;
	
	@ManyToMany(cascade=CascadeType.ALL) 
	public Set<Activity> followedEvents = new HashSet<Activity>(); 
	
	public User(String name, String email, String mobileNr, String password) {
		this.name = name;
        this.email = email;
        this.mobileNr = mobileNr;
        this.password = password;
    }
	
	public static User findUser(String email, String password) {
	    return find("byEmailAndPassword", email, password).first();
	}
}
