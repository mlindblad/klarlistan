package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;


@Entity
public class ActivityStatus extends Model {

	@OneToOne
	public User user;
	
	@ManyToOne
	public Activity activity;
	
	public int status;
	
	public ActivityStatus(User user, Activity activity, int status) {
		this.user = user;
		this.activity = activity;
		this.status = status;
	}
	
	public static List<ActivityStatus> findAllYes(Activity activity) {
	    return ActivityStatus.find("Select as from ActivtyStaus as where as.activity = activity and as.status = 1").fetch();
	}
	
	public static List<ActivityStatus> findAllActivityStatusesForUser(User user) {
		return ActivityStatus.find("byUser", user).fetch();
	}

	
}
