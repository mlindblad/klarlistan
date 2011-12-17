package controllers;

import play.*;
import play.data.validation.Required;
import play.data.validation.Validation.ValidationResult;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPABase;
import play.mvc.*;

import java.util.*;

import models.*;


public class Application extends Controller {

    public static void index() {
    	redirect("Secure.login");
    }
    
    public static void signup() {
    	render();
    }

    public static void createUser(@Required(message="Ange namn") String user_name,
    		@Required(message="Ange email") String user_email,
    		@Required(message="Ange lösenord") String user_password,
    		@Required(message="Ange lösenord") String user_password_confirmation
    		) {
    	
    	
    	ValidationResult validationResult = validation.email(user_email);
    	validationResult.message("Ogiltig Emailadress");
    	validationResult = validation.equals(user_password, user_password_confirmation);
    	validationResult.message("Lösenord och bekräfta lösenord ska vara samma");
    	
    	User user = User.findUserByUsername(user_email);
    	if (user != null) {
    		validation.addError(user_email, "Det finns redan en användera med email-adress %s", user_email);
    	} 

		if (validation.hasErrors()) {
			render("Application/signup.html");
		}
    	
    	
    	user = new User(user_name, user_email, "1234567", user_password);
    	user.save();
    	
    	flash.success("Din användare är nu skapad. Nu kan du logga in");
    	//Send to login
    	redirect("Secure.login");
    }
    
}