package controllers;

import java.util.Date;
import java.util.List;

import models.Activity;
import models.ActivityMessage;
import models.ActivityStatus;
import models.User;
import play.db.jpa.JPABase;
import play.mvc.Controller;

public class Activities extends Controller {

	
	public static void show(Long activityId) {
    	User user = User.findUser("martin.lindblad@gmail.com", "secret");
		Activity activity = Activity.findById(activityId);
		ActivityStatus actStatus = ActivityStatus.find("byUser", user).first();
		List<ActivityMessage> actMessages = ActivityMessage.find("byActivity", activity).fetch();
		
		render(activity, actStatus, actMessages);
    }
	
	public static void list() {
		List<Activity> activities = Activity.findAll();
		render(activities);
	}
	
	public static void newActivity() {
		render();
	}
	
	public void createActivity() {
		
	}
    
    public static void form() {
	    render();
	}
	 
	public static void save(String name, String location) {
		Activity activity = new Activity(name, location, new Date(), "Kom i tid", User.findUser("martin.lindblad@gmail.com", "secret"));
		activity.save();
		show(activity.id);
	}
}
