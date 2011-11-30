import org.junit.*;

import java.util.*;
import java.util.zip.DataFormatException;

import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	@Test
	public void createAndRetrieveUser() {
	    // Create a new user and save it
	    new Activity("Bandy", "Åsen", null, "Minst sex pers").save();
	    
	    // Retrieve the user with e-mail address bob@gmail.com
	    Activity activity = Activity.find("byName", "Bandy").first();
	    
	    // Test 
	    assertNotNull(activity);
	    assertEquals("Bandy", activity.name);
	}
	
	@Test
	public void tryConnectAsUser() {
	    // Create a new user and save it
	    new User("Martin" ,"martin.lindblad@gmail.com", "08635472", "secret").save();
	    
	    // Test 
	    assertNotNull(User.findUser("martin.lindblad@gmail.com", "secret"));
	    assertNull(User.findUser("martin.lindblad@gmail.com", "badpassword"));
	    assertNull(User.findUser("tom@gmail.com", "secret"));
	}
}
