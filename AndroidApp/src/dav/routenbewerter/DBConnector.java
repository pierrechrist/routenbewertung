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
	public void openDB() {	//÷ffnet die lokale DB
		db = Db4o.openFile(this.activitiy.getDir("davRB", Context.MODE_PRIVATE) + "/dav_rb.db");
	}
	
	public void closeDB() {	//Schlieﬂt die lokale DB
		db.close();
	}
	
	public void syncDatabase() {	//Startet ein AsyncTask um die lokale DB mit der entfernten DB zu synchronisieren
        new DBSync().execute(db);
	}
	
	public void mysqlInsert(String sql) {	//Startet ein AsyncTask um ein insert in der entfernten DB vorzunehmen
        new DBInsert().execute(sql);
	}
	
	public ObjectSet<Route> getRoutes(Route r) {	//Holt alle Routen aus der lokalen DB
		ObjectSet<Route> result = null;
		result = db.queryByExample(r);
		return result;
	}
	
	public ObjectSet<User> getUsers(User u) {	//Holt alle User aus der lokalen DB
		ObjectSet<User> result = null;
		result = db.queryByExample(u);
		return result;
	}
	
	public ObjectSet<Rating> getRatings(Rating r) {	//Holt alle Ratings aus der lokalen DB
		ObjectSet<Rating> result = null;
		result = db.queryByExample(r);
		return result;
	}
	
	public Route getRoute(Route r) {	//Holt eine bestimmte Route aus der lokalen DB
		ObjectSet<Route> result = null;
		result = db.queryByExample(r);
		Route route = result.get(0);
		return route;
	}
	
	public User getUser(User u) {	//Holt einen bestimmten User aus der lokalen DB
		ObjectSet<User> result = null;
		result = db.queryByExample(u);
		User user = result.get(0);
		return user;
	}
	
	public Rating getRating(Rating r) {	//Holt ein bestimmtes Rating aus der lokalen DB
		ObjectSet<Rating> result = null;
		result = db.queryByExample(r);
		Rating rating = result.get(0);
		return rating;
	}
	
	public void registerUser(String eMail, String userName, String password) {
		User u = new User(eMail, userName, password);	//User zum Speichern in die Datenbank anlegen
		db.store(u);	//User in der lokalen DB speichern
		
		//User in die entfernte DB schreiben
		mysqlInsert("INSERT INTO `dav_rb`.`rb_user` (`uid`, `user_name`, `user_email`, `user_password`, `route_count`, `flash_count`, `redpoint_count`, `notclimbed_count`) VALUES (NULL, '"+userName+"', '"+eMail+"', '"+password+"', NULL, NULL, NULL, NULL)");
	}
	
	public void setRouteRating(String rating, String howClimbed, String categorie,
			Date date, User user, Route route) {
		Rating r = new Rating(rating, howClimbed, categorie, date, user, route, false);	//Rating zum Speichern in die Datenbank anlegen
		db.store(r);	//Rating in der lokalen DB speichern
		
		//Rating in die entfernte DB schreiben
		mysqlInsert("INSERT INTO `dav_rb`.`rb_ratings` (`rating`, `howclimbed`, `categorie`, `crdate`, `user_id`, `route_id`, `uid`) VALUES ('174', '"+howClimbed+"', '"+categorie+"', NOW(), '"+user.userId+"', '"+route.routeNumber+"', NULL);");
	}
}
