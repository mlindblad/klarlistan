package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.data.validation.Email;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	@Required
	@MinSize(4)
	public String name;
	
	@Required
	@Email
	public String email;
	public String mobileNr;
	
	@Required
	public String password;
	
//	@ManyToMany 
//	public List<Activity> followedActivities = new ArrayList<Activity>(); 
	
	public User(String name, String email, String mobileNr, String password) {
		this.name = name;
        this.email = email;
        this.mobileNr = mobileNr;
        this.password = password;
        
    }
	
	public static User findUser(String email, String password) {
	    return find("byEmailAndPassword", email, password).first();
	}
	
	public static User findUserByUsername(String userName) {
		return find("byEmail", userName).first();
	}
	
	public String toString() {
	    return name;
	}
}
