package dav.routenbewerter;
import java.util.Date;

import android.app.Activity;
import android.content.Context;

import com.db4o.*;

public class DBConnector {
	ObjectContainer db = null;
	Activity activitiy = null;	

	public DBConnector(Activity a) {
		this.activitiy = a;
		this.openDB();
	}
	
	@SuppressWarnings("deprecation")
	public void openDB() {
		db = Db4o.openFile(this.activitiy.getDir("mdp5", Context.MODE_PRIVATE) + "/dav_rb.db");
	}
	
	public void closeDB() {
		db.close();
	}
	
	public void syncDatabase() {	//Startet ein AsyncTask um die lokale DB mit der entfernten DB zu synchronisieren
        new DBSync().execute(db);
	}
	
	public ObjectSet<Route> getRoutes(Route r) {
		ObjectSet<Route> result = null;
		result = db.queryByExample(r);
		return result;
	}
	
	public ObjectSet<User> getUsers(User u) {
		ObjectSet<User> result = null;
		result = db.queryByExample(u);
		return result;
	}
	
	public ObjectSet<Rating> getRatings(Rating r) {
		ObjectSet<Rating> result = null;
		result = db.queryByExample(r);
		return result;
	}
	
	public Route getRoute(Route r) {
		ObjectSet<Route> result = null;
		result = db.queryByExample(r);
		Route route = result.get(0);
		return route;
	}
	
	public User getUser(User u) {
		ObjectSet<User> result = null;
		result = db.queryByExample(u);
		User user = result.get(0);
		return user;
	}
	
	public Rating getRating(Rating r) {
		ObjectSet<Rating> result = null;
		result = db.queryByExample(r);
		Rating rating = result.get(0);
		return rating;
	}
	
	public void registerUser(String eMail, String userName, String password) {
		User u = new User(eMail, userName, password);	//User zum Speichern in die Datenbank anlegen
		db.store(u);	//User in der lokalen DB speichern
	}
	
	public void setRouteRating(String rating, String howClimbed, String categorie,
			Date date, User user, Route route) {
		Rating r = new Rating(rating, howClimbed, categorie, date, user, route, false);	//Rating zum Speichern in die Datenbank anlegen
		db.store(r);	//Rating in der lokalen DB speichern
	}
}
