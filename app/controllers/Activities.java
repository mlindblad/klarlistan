package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import models.Activity;
import models.ActivityMessage;
import models.ActivityStatus;
import models.User;
import models.UserFriend;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.libs.Mail;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import play.mvc.With;

@CRUD.For(Activity.class)
@With(Secure.class)
public class Activities extends CRUD {

	
	public static void show(Long activityId) {
		Activity activity = Activity.findById(activityId);
		User user = User.findUserByUsername(Security.connected());
		ActivityStatus actStatus = ActivityStatus.find("byUserAndActivity", user, activity).first();
		
		List<ActivityMessage> actMessages = ActivityMessage.find("activity = ? order by date desc", activity).fetch();
		
		render(activity, actStatus, actMessages);
    }
	
	public static void updateStatus(Long activityId, int newstatus) {
		Activity activity = Activity.findById(activityId);
		User user = User.findUserByUsername(Security.connected());
		ActivityStatus actStatus = ActivityStatus.find("byUserAndActivity", user, activity).first();
		actStatus.status = newstatus;
		actStatus.save();
		show(activityId);
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
		List<User> friends = new ArrayList<User>();
		List<UserFriend> UserFriends = UserFriend.find("byUserId", Security.connected()).fetch();
		for (UserFriend userFriend : UserFriends) {
			friends.add(User.findUserByUsername(userFriend.friendId));
		}
		render(friends);
	}
	
	public void createActivity() {
		
	}
    
    public static void form() {
	    render();
	}
	 
	public static void save(@Required(message="Ange ett namn på aktiviteten") String name, 
			@Required(message="Ange plats") String location, 
			@Required(message="Ange datum") String date, String information) {

		
		if (validation.hasErrors()) {
			render("Activities/newActivity.html");
		}
		
		
		// The constructor is the format the string will take
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date activityDate = null;
		try {
			activityDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Activity activity = new Activity(name, location, activityDate, information, User.findUserByUsername(Security.connected()));
		activity.save();
		
		for (String friend : params.getAll("friends")) {
			System.out.println("new friend: " + friend);
			System.out.println("Activities " + User.findUserByUsername(friend));
			ActivityStatus status = new ActivityStatus(User.findUserByUsername(friend), activity, -1);
			status.save();
			//Send an email
			try {
			SimpleEmail email = new SimpleEmail();
			email.setFrom("info@klarlistan.nu");
				email.addTo("martin.lindblad@gmail.com");
			email.setSubject("du har blivit inbjuden till en aktivitet");
			email.setMsg("Funkar verkligen detta?");
			Mail.send(email); 
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ActivityStatus status = new ActivityStatus(User.findUserByUsername(Security.connected()), activity, 1);
		status.save();
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
