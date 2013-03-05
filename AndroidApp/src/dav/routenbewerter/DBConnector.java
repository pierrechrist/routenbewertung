package dav.routenbewerter;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.db4o.*;

public class DBConnector {
	private ObjectContainer db = null;
	private Activity activitiy = null;	
	private ProgressDialog progressDialog = null;
	private String result = "";

	public DBConnector(Activity a) {
		this.activitiy = a;
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
		 
		try {
			result = new JSONParser(activitiy).execute(basicNameValuePair).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//start the progress dialog

		progressDialog = ProgressDialog.show(activitiy, "", "Datenbank Sync...");
		new Thread() {
			public void run() {
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
				
				// dismiss the progress dialog
				progressDialog.dismiss();
		}

		}.start();
	}
	
	public ObjectSet<Route> getRoutes(Route r) {	//Holt alle Routen aus der lokalen DB
		ObjectSet<Route> result = db.queryByExample(r);
		return result;
	}
	
	public ObjectSet<User> getUsers(User u) {	//Holt alle User aus der lokalen DB
		ObjectSet<User> result = db.queryByExample(u);
		return result;
	}
	
	public ObjectSet<Rating> getRatings(Rating r) {	//Holt alle Ratings aus der lokalen DB
		ObjectSet<Rating> result = db.queryByExample(r);
		return result;
	}
	
	public Route getRoute(Route r) {	//Holt eine bestimmte Route aus der lokalen DB
		ObjectSet<Route> result = db.queryByExample(r);
		Route route = null;
		if(!result.isEmpty()){
			route = result.next();
		}
		return route;
	}
	
	public User getUser(User u) {	//Holt einen bestimmten User aus der lokalen DB
		ObjectSet<User> result = db.queryByExample(u);
		User user = null;
		if(!result.isEmpty()){
			user = result.next();
		}
		return user;
	}
	
	public Rating getRating(Rating r) {	//Holt ein bestimmtes Rating aus der lokalen DB
		ObjectSet<Rating> result = db.queryByExample(r);
		Rating rating = null;
		if(!result.isEmpty()){
			rating = result.next();
		}
		return rating;
	}
	
	public Boolean registerUser(String eMail, String userName, String password) {
		Boolean success = false;
		
		//User in die entfernte DB schreiben
		BasicNameValuePair tag = new BasicNameValuePair("tag","register");
		BasicNameValuePair pairUserName = new BasicNameValuePair("name",userName);
		BasicNameValuePair pairEmail = new BasicNameValuePair("email",eMail);
		BasicNameValuePair pairUserPassword = new BasicNameValuePair("password",password);
		 
        String result = "";
		try {
			AsyncTask<BasicNameValuePair, Integer, String> parser = new JSONParser(activitiy).execute(tag, pairUserName, pairEmail, pairUserPassword);
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
        	Log.i("DAV", "registerUser_success: " + jObj.getString("success"));
			Log.i("DAV", "registerUser_error: " + jObj.getString("error"));
			if(jObj.getString("success").equals("1")){
				JSONObject jsonUser = jObj.getJSONObject("user");
				Log.i("DAV", "setRouteRating_name: " + jsonUser.getString("name"));
				Log.i("DAV", "setRouteRating_email: " + jsonUser.getString("email"));
			}
			if(jObj.getString("success").equals("1")) {
				success = true;
			} else {
				Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
				success = false;
			}
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		checkUser(userName, password);
		return success;
		
	}
	
	public int checkUser(String name, String password) {
		BasicNameValuePair tag = new BasicNameValuePair("tag","login");
		BasicNameValuePair userName = new BasicNameValuePair("name",name);
		BasicNameValuePair userPassword = new BasicNameValuePair("password",password);
		 
        int userId = 0;
		try {
			AsyncTask<BasicNameValuePair, Integer, String> parser = new JSONParser(activitiy).execute(tag, userName, userPassword);
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
        	Log.i("DAV", "checkUser_success: " + jObj.getString("success"));
			Log.i("DAV", "checkUser_error: " + jObj.getString("error"));
			if(jObj.getString("success").equals("1")) {
				if(jObj.getString("success").equals("1")){
					JSONObject jsonUser = jObj.getJSONObject("user");
					Log.i("DAV", "setRouteRating_uid: " + jsonUser.getInt("uid"));
					Log.i("DAV", "setRouteRating_name: " + jsonUser.getString("name"));
					Log.i("DAV", "setRouteRating_email: " + jsonUser.getString("email"));
					userId = jsonUser.getInt("uid");
					User u = new User(jsonUser.getInt("uid"), jsonUser.getString("email"), jsonUser.getString("name"));	//User zum Speichern in lokale die Datenbank anlegen
					ObjectSet<User> dbresult = db.queryByExample(u);
					if(!dbresult.isEmpty()){	//Wenn es den Eintrag schon in der DB gibt wird er nur geupdated
						User found = dbresult.next();
						found.setUserId(jsonUser.getInt("uid"));
						found.setUserName(jsonUser.getString("name"));
						found.seteMail(jsonUser.getString("email"));
						db.store(found);
					}
					else {	//Ansonsten wird ein neuer Eintrag angelegt
						db.store(u);
					}
				}
			} else {
				Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
			}
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

		Log.i("DAV", "Uid: " + userId);
		return userId;
	}
	
	public Boolean setRouteRating(String rating, String howClimbed, String categorie,
			Date date, User user, Route route) {
		Boolean success = false;
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
			AsyncTask<BasicNameValuePair, Integer, String> parser = new JSONParser(activitiy).execute(tag, pairRouteId, pairUserId, pairCategorie, pairHowClimbed, pairRating);
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
        	Log.i("DAV", "setRouteRating_success: " + jObj.getString("success"));
			Log.i("DAV", "setRouteRating_error: " + jObj.getString("error"));
			if(jObj.getString("success").equals("1")) {
				success = true;
			} else {
				Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
				success = false;
			}
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		return success;
	}
}
