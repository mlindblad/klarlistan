package controllers;

import play.mvc.Controller;
import sun.swing.SwingUtilities2.Section;
import models.UserFriend;
import models.User;

public class Users extends Controller {    
	
	public static void search() {
//		render("UserFriends/listAll.html");
	}
	
	public static void showAccountInfo() {
		User user = User.findUserByUsername(Security.connected()); 
		render(user);
	}
}