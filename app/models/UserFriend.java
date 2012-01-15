package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class UserFriend extends Model {

	public String userId;

	public String friendId; 
	
	public UserFriend(String userId, String friendId) {
		this.userId = userId;
		this.friendId = friendId;
	}
}
