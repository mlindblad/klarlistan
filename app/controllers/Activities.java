package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Activity;
import models.ActivityMessage;
import models.ActivityStatus;
import models.User;
import play.db.jpa.JPABase;
import play.mvc.Controller;
import play.mvc.With;

@CRUD.For(Activity.class)
@With(Secure.class)
public class Activities extends CRUD {

	
	public static void show(Long activityId) {
		Activity activity = Activity.findById(activityId);
		User user = User.findUserByUsername(Security.connected());
		ActivityStatus actStatus = ActivityStatus.find("byUserAndActivity", user, activity).first();
		List<ActivityMessage> actMessages = ActivityMessage.find("byActivity", activity).fetch();
		
		render(activity, actStatus, actMessages);
    }
	
	public static void listAll() {
		User user = User.findUserByUsername(Security.connected());
		List<ActivityStatus> statuses = ActivityStatus.findAllActivityStatusesForUser(user);
		List<Activity> activities = new ArrayList<Activity>();
		for (ActivityStatus activityStatus : statuses) {
			activities.add(activityStatus.activity);
		}
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
	
	public static void createActivityMessage(Long activityId, String message) {
		Activity activity = Activity.findById(activityId);
		User user = User.findUserByUsername(Security.connected());
		ActivityMessage actMessage = new ActivityMessage(message, new Date(), activity, user);
		actMessage.save();
		show(activityId);
	}
}
