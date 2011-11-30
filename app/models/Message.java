package models;

import java.util.Date;

import javax.persistence.ManyToOne;

import play.db.jpa.Model;

public class Message extends Model {

	public String message;
	public Date date;
	
	@ManyToOne
	public Activity activity;
	
	@ManyToOne
	public User user;
	
	public Message(String message, Date date, Activity activity, User user) {
		this.message = message;
		this.date = date;
		this.activity = activity;
		this.user = user;
	}
	
}
