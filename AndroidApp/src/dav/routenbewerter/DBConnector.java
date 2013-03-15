package dav.routenbewerter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.db4o.*;

public class DBConnector {
	private ObjectContainer db = null;
	private Activity activitiy = null;	
	private ProgressDialog progressDialog = null;
	private String result = "";
	private String routes = "";
	private String ratings = "";
	private DBConnector dbC = null;
	private NetworkInfo ni = null;
	ConnectivityManager cm = null;

	public DBConnector(Activity a) {
		this.activitiy = a;
		dbC = this;
		cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
		ni = cm.getActiveNetworkInfo();
	}
	
	@SuppressWarnings("deprecation")
	public void openDB() {	//÷ffnet die lokale DB
		db = Db4o.openFile(this.activitiy.getDir("davRB", Context.MODE_PRIVATE) + "/dav_rb.db");
	}
	
	public void closeDB() {	//Schlieﬂt die lokale DB
		db.close();
	}
	
	public Boolean isOnline() {
		if(ni != null && ni.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void syncDB(int userId) {	//Startet ein AsyncTask um die lokale DB mit der entfernten DB zu synchronisieren
		if(isOnline()) { 
			BasicNameValuePair getRoutesPair = new BasicNameValuePair("tag","getroutes");
			BasicNameValuePair getRatingsPair = new BasicNameValuePair("tag","getratings");
			BasicNameValuePair userIdPair = new BasicNameValuePair("userid",userId+"");
			 
			try {
				routes = new JSONParser(activitiy).execute(getRoutesPair).get();
				ratings = new JSONParser(activitiy).execute(getRatingsPair, userIdPair).get();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//start the progress dialog
	
			progressDialog = ProgressDialog.show(activitiy, "", "DB Sync...");
			new Thread() {
				@SuppressLint("SimpleDateFormat")
				public void run() {
					//Routen in die DB schreiben
					try {
						JSONObject jObj = new JSONObject(routes);
						Log.i("DAV", "routeSnyc_success: " + jObj.getString("success"));
						Log.i("DAV", "routeSnyc_error: " + jObj.getString("error"));
						JSONArray jArray = jObj.getJSONArray("route");
					    for(int i=0; i < jArray.length(); i++){
							JSONObject json_data = jArray.getJSONObject(i);
	
							//db4o
							Route r = new Route(json_data.getInt("uid"));
							ObjectSet<Route> dbresult = db.queryByExample(r);
							
							String avarageRating = null;
							if(json_data.getString("avarage_rating").equals("null"))
								avarageRating = json_data.getString("uiaa");
							else
								avarageRating = json_data.getString("avarage_rating");
							
							if(!dbresult.isEmpty()){	//Wenn es den Eintrag schon in der DB gibt wird er nur geupdated
								Route found = dbresult.next();
								found.setAverageRating(avarageRating);
								found.setAvarageCategorie(json_data.getString("avarage_categorie"));
								found.setFlashCount(json_data.getInt("flash_count"));
								found.setRedpointCount(json_data.getInt("redpoint_count"));
								found.setNotClimbedCount(json_data.getInt("not_climbed_count"));
								db.store(found);
							}
							else {	//Ansonsten wird ein neuer Eintrag angelegt
								r = new Route(json_data.getInt("uid"), json_data.getString("color"), json_data.getString("createdby"), 
										json_data.getString("sektor"), json_data.getInt("dateon"), json_data.getInt("tr"), json_data.getInt("boltrow"), avarageRating, json_data.getInt("rating_count"),
										json_data.getString("avarage_categorie"), json_data.getInt("flash_count"), json_data.getInt("redpoint_count"), json_data.getInt("project_count"), json_data.getInt("not_climbed_count"));
								db.store(r);
							}
						}
					} catch (JSONException e){
						Log.e("log_tag", "Error parsing data "+e.toString());
					}
					
					//Ratings in die DB schreiben
					try {
						JSONObject jObj = new JSONObject(ratings);
						Log.i("DAV", "ratingSnyc_success: " + jObj.getString("success"));
						Log.i("DAV", "ratingSnyc_error: " + jObj.getString("error"));
						JSONArray jArray = jObj.getJSONArray("rating");
					    for(int i=0; i < jArray.length(); i++){
							JSONObject json_data = jArray.getJSONObject(i);
	
							//db4o
							Rating a = new Rating(dbC.getRoute(new Route(json_data.getInt("route_id"))), dbC.getUser(new User(json_data.getInt("user_id"))));
							ObjectSet<Rating> dbresult = db.queryByExample(a);
							
							if(dbresult.isEmpty()) {	
								SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						        Date date = null;
								try {									   
									date = sdfToDate.parse(json_data.getString("crdate"));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								a = new Rating(json_data.getString("uiaa"), json_data.getString("howclimbed"), json_data.getString("categorie"),
										date, dbC.getUser(new User(json_data.getInt("user_id"))), dbC.getRoute(new Route(json_data.getInt("route_id"))), true);
								db.store(a);
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
	}
	
	public ObjectSet<Route> getRoutes(Route r) {	//Holt alle Routen die dem suchparameter entsprechen
		ObjectSet<Route> result = db.queryByExample(r);
		return result;
	}
	
	public ObjectSet<Route> getRoutes() {	//Holt alle Routen aus der lokalen DB
		ObjectSet<Route> result = db.queryByExample(Route.class);
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
	
	public int registerUser(String eMail, String userName, String password) {
		int userId = 0;
		
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
				Log.i("DAV", "registerUser_name: " + jsonUser.getString("name"));
				Log.i("DAV", "registerUser_email: " + jsonUser.getString("email"));
			}
			if(jObj.getString("success").equals("1")) {
				Toast.makeText(activitiy, "Erfolgreich Registriert", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
			}
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		userId = checkUser(userName, password);
		return userId;	
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
					Log.i("DAV", "checkUser_uid: " + jsonUser.getInt("uid"));
					Log.i("DAV", "checkUser_name: " + jsonUser.getString("name"));
					Log.i("DAV", "checkUser_email: " + jsonUser.getString("email"));
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
	
	@SuppressLint("SimpleDateFormat")
	public Boolean setRouteRating(String rating, String howClimbed, String categorie, int userId, int routeId) {
		Boolean success = false;
		
		if(isOnline()) {
			//Rating in die entfernte DB schreiben
			BasicNameValuePair tag = new BasicNameValuePair("tag","setrating");
			BasicNameValuePair pairRouteId = new BasicNameValuePair("routeid",routeId+"");
			BasicNameValuePair pairUserId = new BasicNameValuePair("userid",userId+"");
			BasicNameValuePair pairCategorie = new BasicNameValuePair("categorie",categorie);
			BasicNameValuePair pairHowClimbed = new BasicNameValuePair("howclimbed",howClimbed);
			BasicNameValuePair pairRating = new BasicNameValuePair("rating",getUiaa(rating)+"");
			
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
					SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        Date date = null;
			        JSONObject jsonRating = jObj.getJSONObject("rating");
					try {									   
						date = sdfToDate.parse(jsonRating.getString("created_at"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Route r = this.getRoute(new Route(routeId));
					Rating a = new Rating(rating, howClimbed, categorie, date, this.getUser(new User(userId)), r, true);	//Rating zum Speichern in die Datenbank anlegen
					db.store(a);	//Rating in der lokalen DB speichern
					if(howClimbed.equals("Flash")) {
						r.setFlashCount(r.getFlashCount()+1);
					} else if(howClimbed.equals("Rotpunkt")) {
						r.setRedpointCount(r.getRedpointCount()+1);
					} else {
						r.setProjectCount(r.getProjectCount()+1);
					}
					r.setNotClimbedCount(r.getNotClimbedCount()-1);
					db.store(r);
				} else {
					Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
					success = false;
				}
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
		} else {
			Route r = this.getRoute(new Route(routeId));
			Rating a = new Rating(rating, howClimbed, categorie, null, this.getUser(new User(userId)), r, false);	//Rating zum Speichern in die Datenbank anlegen
			db.store(a);	//Rating in der lokalen DB speichern
			if(howClimbed.equals("Flash")) {
				r.setFlashCount(r.getFlashCount()+1);
			} else if(howClimbed.equals("Rotpunkt")) {
				r.setRedpointCount(r.getRedpointCount()+1);
			} else {
				r.setProjectCount(r.getProjectCount()+1);
			}
			r.setNotClimbedCount(r.getNotClimbedCount()-1);
			db.store(r);
		}
		
		return success;
	}
	
	public int getUiaa(String rating) {
		BasicNameValuePair tag = new BasicNameValuePair("tag","getuiaa");
		BasicNameValuePair pairRating = new BasicNameValuePair("rating",rating);
		int uiaa = 0;

		String result = "";
		try {
			Log.i("DAV", "next up AsyncTask");
			AsyncTask<BasicNameValuePair, Integer, String> parser = new JSONParser(activitiy).execute(tag, pairRating);
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
        	Log.i("DAV", "getUiaa_success: " + jObj.getString("success"));
			Log.i("DAV", "getUiaa_error: " + jObj.getString("error"));
			if(jObj.getString("success").equals("1")) {
				uiaa = jObj.getInt("uiaa");
			} else {
				Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
				uiaa = 166;
			}
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		return uiaa;
	}
}
