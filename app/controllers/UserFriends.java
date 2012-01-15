package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import models.User;
import models.UserFriend;

public class UserFriends extends CRUD {

	public static void listAll() {
		User findUserByUsername = User.findUserByUsername(Security.connected());
		List<User> listOfFriends = new ArrayList<User>();
		List<UserFriend> friends = UserFriend.find("byUserId", findUserByUsername.email).fetch();
		for (UserFriend userFriendNew : friends) {
			listOfFriends.add(getUser(userFriendNew.friendId));
		}
		render(listOfFriends);
	}
	
	private static User getUser(String email) {
		return User.find("byEmail", email).first();
	}
	
	public static void search(String pattern) {
		List<User> users = User.findAll();
		List<User> possibleFriends = findPossibleFriends(users, pattern);
		render(possibleFriends);
		 
	}
	
	private static List<User> findPossibleFriends(List<User> allUsers, String pattern) {
		List<User> possibleFriends = new ArrayList<User>();
		for (User user : allUsers) {
			boolean isMatch = Pattern.matches(".*"+pattern+".*", user.name);
			if (isMatch && notFriends(user) && notMe(user)) {
				possibleFriends.add(user);
			}
		}
		return possibleFriends;
	}
	
	private static boolean notMe(User user) {
		return !getLoggedInUser().email.equals(user.email);
	}

	private static boolean notFriends(User user) {
		List<UserFriend> userFriends = UserFriend.find("byUserId", getLoggedInUser().email).fetch();
		for (UserFriend userFriend : userFriends) {
			if (userFriend.friendId.equals(user.email)) {
				return false;
			}
		}
		return true;
	}
	
	public static void addFriend(User friend) {
		User user = User.findUserByUsername(Security.connected());
		user.addFriend(friend);
		listAll();
	}
	
	private static User getLoggedInUser() {
		return User.findUserByUsername(Security.connected());
	}
}
