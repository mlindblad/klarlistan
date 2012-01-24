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

@With(Secure.class)
public class Activities extends Controller {

	public static void show(Long activityId) {
		Activity activity = Activity.findById(activityId);
		User user = User.findUserByUsername(Security.connected());
		ActivityStatus actStatus = ActivityStatus.find("byUserAndActivity",
				user, activity).first();

		List<ActivityMessage> actMessages = ActivityMessage.find(
				"activity = ? order by date desc", activity).fetch();

		render(activity, actStatus, actMessages);
	}

	public static void updateStatus(Long activityId, int newstatus) {
		Activity activity = Activity.findById(activityId);
		User user = User.findUserByUsername(Security.connected());
		ActivityStatus actStatus = ActivityStatus.find("byUserAndActivity",
				user, activity).first();
		actStatus.status = newstatus;
		actStatus.save();
		show(activityId);
	}

	public static void listAll() {
		User user = User.findUserByUsername(Security.connected());
		List<ActivityStatus> statuses = ActivityStatus
				.findAllActivityStatusesForUser(user);
		List<Activity> activities = new ArrayList<Activity>();
		for (ActivityStatus activityStatus : statuses) {
			activities.add(activityStatus.activity);
		}

		render(activities, user);
	}

	public static void newActivity() {
		List<User> friends = new ArrayList<User>();
		List<UserFriend> UserFriends = UserFriend.find("byUserId",
				Security.connected()).fetch();
		for (UserFriend userFriend : UserFriends) {
			friends.add(User.findUserByUsername(userFriend.friendId));
		}
		render(friends);
	}

	public static void edit(Long activityId) {
		Activity activity = Activity.findById(activityId);
		List<User> friends = new ArrayList<User>();
		List<UserFriend> UserFriends = UserFriend.find("byUserId",
				Security.connected()).fetch();
		for (UserFriend userFriend : UserFriends) {
			friends.add(User.findUserByUsername(userFriend.friendId));
		}

		List<User> actUsers = new ArrayList<User>();
		List<ActivityStatus> actStatuses = activity.activityStatuses;
		for (ActivityStatus activityStatus : actStatuses) {
			actUsers.add(activityStatus.user);
		}
		render(activity, friends, actUsers);
	}

	public static void update(Long activityId,
			@Required(message = "Ange ett namn på aktiviteten") String name,
			@Required(message = "Ange plats") String location,
			@Required(message = "Ange datum") String date, String information, String[] friends) {

		if (validation.hasErrors()) {
			render("Activities/edit.html");
		}

		Date activityDate = parseDate(date);

		Activity activity = Activity.findById(activityId);
		activity.name = name;
		activity.location = location;
		activity.date = activityDate;
		activity.information = information;
		activity.save();
		
		if (friends != null) {
			updateParticipants(friends, activity);
		}
		
		show(activityId);
	}

	private static Date parseDate(String date) {
		// The constructor is the format the string will take
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date activityDate = null;
		try {
			activityDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activityDate;
	}

	private static void updateParticipants(String[] friends, Activity activity) {
		List<ActivityStatus> actStatuses = activity.activityStatuses;
		boolean found = false;
		for (String participant : friends) {
			for (ActivityStatus status : actStatuses) {
				if (participant.equals(status.user.email)) {
					found = true;
				}
			}
			if (!found) {
				ActivityStatus status = new ActivityStatus(User.findUserByUsername(participant), activity, -1);
				status.save();
				createAndSendMail(participant);
			}
			found = false;
		}
	}

	public void createActivity() {
		
	}

	public static void form() {
		render();
	}

	public static void save(
			@Required(message = "Ange ett namn på aktiviteten") String name,
			@Required(message = "Ange plats") String location,
			@Required(message = "Ange datum") String date, String information, String[] friends) {

		if (validation.hasErrors()) {
			render("Activities/newActivity.html");
		}

		Date activityDate = parseDate(date);

		Activity activity = new Activity(name, location, activityDate,
				information, User.findUserByUsername(Security.connected()));
		activity.save();

		for (String friend : params.getAll("friends")) {
			ActivityStatus status = new ActivityStatus(User.findUserByUsername(friend), activity, -1);
			status.save();
			createAndSendMail(friend);
		}

		ActivityStatus status = new ActivityStatus(
				User.findUserByUsername(Security.connected()), activity, 1);
		status.save();
		show(activity.id);
	}

	public static void createActivityMessage(Long activityId, String message) {
		Activity activity = Activity.findById(activityId);
		User user = User.findUserByUsername(Security.connected());
		ActivityMessage actMessage = new ActivityMessage(message, new Date(),
				activity, user);
		actMessage.save();
		show(activityId);
	}

	private static void createAndSendMail(String emailAddress) {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setFrom("info@klarlistan.nu");
			email.addTo(emailAddress);
			email.setSubject("du har blivit inbjuden till en aktivitet");
			email.setMsg("Funkar verkligen detta?");
			Mail.send(email);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
