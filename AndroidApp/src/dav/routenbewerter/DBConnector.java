package dav.routenbewerter;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
		 BasicNameValuePair basicNameValuePair = new BasicNameValuePair("tag","getroutes");
		 
        String result = "";
		try {
			result = new JSONParser().execute(basicNameValuePair).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//JSON Daten parsen
		try {
			JSONObject jObj = new JSONObject(result);
			Log.i("DAV", "snyc_success: " + jObj.getString("success"));
			Log.i("DAV", "snyc_error: " + jObj.getString("error"));
			JSONArray jArray = jObj.getJSONArray("route");
		    for(int i=0; i < jArray.length(); i++){
				JSONObject json_data = jArray.getJSONObject(i);

				//db4o
				Route r = new Route(json_data.getInt("uid"), null, null, null, 0);
				ObjectSet<Route> dbresult = db.queryByExample(r);
				if(!dbresult.isEmpty()){	//Wenn es den Eintrag schon in der DB gibt wird er nur geupdated
					Route found = dbresult.next();
					found.setCreationDate(json_data.getInt("dateon"));
					found.setRouteDriver(json_data.getString("createdby"));
					found.setHandleColor(json_data.getString("color"));
					found.setRouteDriver(json_data.getString("createdby"));
					db.store(found);
				}
				else {	//Ansonsten wird ein neuer Eintrag angelegt
					r = new Route(json_data.getInt("uid"), json_data.getString("color"), json_data.getString("createdby"), json_data.getString("sektor"), json_data.getInt("dateon"));
					db.store(r);
				}
			}
		} catch (JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
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
		BasicNameValuePair tag = new BasicNameValuePair("tag","register");
		BasicNameValuePair pairUserName = new BasicNameValuePair("name",userName);
		BasicNameValuePair pairEmail = new BasicNameValuePair("email",eMail);
		BasicNameValuePair pairUserPassword = new BasicNameValuePair("password",password);
		 
        String result = "";
		try {
			AsyncTask<BasicNameValuePair, Integer, String> parser = new JSONParser().execute(tag, pairUserName, pairEmail, pairUserPassword);
			result = (String)parser.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// try parse the string to a JSON object
		JSONObject jObj = null;
		try {
        	jObj = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		try {
			Log.i("DAV", "registerUser_success: " + jObj.getString("success"));
			Log.i("DAV", "registerUser_error: " + jObj.getString("error"));
			JSONObject jsonUser = jObj.getJSONObject("user");
			Log.i("DAV", "setRouteRating_name: " + jsonUser.getString("name"));
			Log.i("DAV", "setRouteRating_email: " + jsonUser.getString("email"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}
	
	public void checkUser(String name, String password) {
		BasicNameValuePair tag = new BasicNameValuePair("tag","login");
		BasicNameValuePair userName = new BasicNameValuePair("name",name);
		BasicNameValuePair userPassword = new BasicNameValuePair("password",password);
		 
        String result = "";
		try {
			AsyncTask<BasicNameValuePair, Integer, String> parser = new JSONParser().execute(tag, userName, userPassword);
			result = (String)parser.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// try parse the string to a JSON object
		JSONObject jObj = null;
		try {
        	jObj = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		try {
			Log.i("DAV", "checkUser_success: " + jObj.getString("success"));
			Log.i("DAV", "checkUser_error: " + jObj.getString("error"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setRouteRating(String rating, String howClimbed, String categorie,
			Date date, User user, Route route) {
		Rating r = new Rating(rating, howClimbed, categorie, date, user, route, false);	//Rating zum Speichern in die Datenbank anlegen
		db.store(r);	//Rating in der lokalen DB speichern
		
		//Rating in die entfernte DB schreiben
		BasicNameValuePair tag = new BasicNameValuePair("tag","setRating");
		BasicNameValuePair pairRouteId = new BasicNameValuePair("routeid",route.getRouteNumber()+"");
		BasicNameValuePair pairUserId = new BasicNameValuePair("userid",user.getUserId()+"");
		BasicNameValuePair pairCategorie = new BasicNameValuePair("categorie",categorie);
		BasicNameValuePair pairHowClimbed = new BasicNameValuePair("howclimbed",howClimbed);
		BasicNameValuePair pairRating = new BasicNameValuePair("rating",rating);
		
		String result = "";
		try {
			Log.i("DAV", "next up AsyncTask");
			AsyncTask<BasicNameValuePair, Integer, String> parser = new JSONParser().execute(tag, pairRouteId, pairUserId, pairCategorie, pairHowClimbed, pairRating);
			result = (String)parser.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// try parse the string to a JSON object
		JSONObject jObj = null;
		try {
        	jObj = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		try {
			Log.i("DAV", "setRouteRating_success: " + jObj.getString("success"));
			Log.i("DAV", "setRouteRating_error: " + jObj.getString("error"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
