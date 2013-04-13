package dav.routenbewerter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dav.routenbewerter.R;
import com.db4o.*;
import com.db4o.query.Query;

public final class DBConnector {
	private ObjectContainer db = null;
	public Activity activitiy = null;
	private NetworkInfo ni = null;
	ConnectivityManager cm = null;
	private Boolean dbOpen = false;

	public DBConnector(Activity a) {
		this.activitiy = a;
		cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
		ni = cm.getActiveNetworkInfo();
	}

	@SuppressWarnings("deprecation")
	public void openDB() { // Öffnet die lokale DB
		db = Db4o.openFile(this.activitiy.getDir("davRB", Context.MODE_PRIVATE) + "/dav_rb.db");
		dbOpen = true;
	}

	public void closeDB() { // Schließt die lokale DB
		db.close();
		dbOpen = false;
	}

	public Boolean isDbOpen() {
		return dbOpen;
	}

	public Boolean isOnline() {
		if (ni != null && ni.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void syncDB(final int userId) { // Startet ein AsyncTask um die lokale DB mit der entfernten DB zu synchronisieren
		if (isOnline()) {
			new DBSync(activitiy, this, db).execute(userId);
			SharedPreferences sp = activitiy.getApplication().getSharedPreferences("Login", Context.MODE_PRIVATE);
			SharedPreferences.Editor ed = sp.edit();
			ed.putLong("syncDate", new Date().getTime());
			ed.commit();
		}

	}

	public ObjectSet<Route> getRoutes(Route r) { // Holt alle Routen die dem suchparameter entsprechen
		ObjectSet<Route> result = db.queryByExample(r);
		return result;
	}

	public ObjectSet<Route> getRoutes(String wallName, String rating, String categorie) { // Holt alle Routen die dem suchparameter entsprechen
		Query query = db.query();
		query.constrain(Route.class);
		query.descend("boltRow").orderAscending();
		if (wallName != null)
			query.descend("wallName").constrain(wallName);
		if (rating != null)
			query.descend("rating").constrain(rating);
		if (categorie != null)
			query.descend("caategorie").constrain(categorie);
		ObjectSet<Route> result = query.execute();
		return result;
	}

	public ObjectSet<Route> getRoutes() { // Holt alle Routen aus der lokalen DB
		Query query = db.query();
		query.constrain(Route.class);
		query.descend("boltRow").orderAscending();

		ObjectSet<Route> result = query.execute();
		return result;
	}

	public ObjectSet<User> getUsers(User u) { // Holt alle User aus der lokalen DB
		ObjectSet<User> result = db.queryByExample(u);
		return result;
	}

	public ObjectSet<Rating> getRatings(Rating r) { // Holt alle Ratings aus der lokalen DB
		ObjectSet<Rating> result = db.queryByExample(r);
		return result;
	}

	public Route getRoute(Route r) { // Holt eine bestimmte Route aus der lokalen DB
		ObjectSet<Route> result = db.queryByExample(r);
		Route route = null;
		if (!result.isEmpty()) {
			route = result.next();
		}
		return route;
	}

	public User getUser(User u) { // Holt einen bestimmten User aus der lokalen DB
		ObjectSet<User> result = db.queryByExample(u);
		User user = null;
		if (!result.isEmpty()) {
			user = result.next();
		}
		return user;
	}

	public Rating getRating(Rating r) { // Holt ein bestimmtes Rating aus der lokalen DB
		ObjectSet<Rating> result = db.queryByExample(r);
		Rating rating = null;
		if (!result.isEmpty()) {
			rating = result.next();
		}
		return rating;
	}

	public List<Route> getOldestRoutes() {
		Query query = db.query();
		query.constrain(Route.class);
		query.descend("creationDate").orderAscending();

		ObjectSet<Route> result = query.execute();
		List<Route> list = null;
		list = new ArrayList<Route>();
		for (int i = 0; i < 10; i++) {
			list.add(result.next());
		}
		return list;
	}

	public List<Route> getNewsestRoutes() {
		Query query = db.query();
		query.constrain(Route.class);
		query.descend("creationDate").orderDescending();

		ObjectSet<Route> result = query.execute();
		List<Route> list = null;
		list = new ArrayList<Route>();
		for (int i = 0; i < 10; i++) {
			list.add(result.next());
		}
		return list;
	}

	public int registerUser(String eMail, String userName, String password) {
		int userId = 0;

		if (isOnline()) {
			// User in die entfernte DB schreiben
			BasicNameValuePair tag = new BasicNameValuePair("tag", "register");
			BasicNameValuePair pairUserName = new BasicNameValuePair("name", userName);
			BasicNameValuePair pairEmail = new BasicNameValuePair("email", eMail);
			BasicNameValuePair pairUserPassword = new BasicNameValuePair("password", password);

			new JSONParser(activitiy, "Registriere Benutzer...") {
				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					this.dialog.dismiss();
					// try parse the string to a JSON object
					JSONObject jObj = null;
					try {
						jObj = new JSONObject(result);
						JSONObject jsonUser = jObj.getJSONObject("user");
						Log.i("DAV", "registerUser_success: " + jObj.getString("success"));
						Log.i("DAV", "registerUser_error: " + jObj.getString("error"));
						if (jObj.getString("success").equals("1")) {
							Log.i("DAV", "registerUser_name: " + jsonUser.getString("name"));
							Log.i("DAV", "registerUser_email: " + jsonUser.getString("email"));
						}
						if (jObj.getString("success").equals("1")) {
							Toast.makeText(activitiy, "Erfolgreich Registriert", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
						}
						if (jsonUser.getString("uid") != "0") {
							Intent menuActivity = new Intent(activitiy.getApplicationContext(), MenuActivity.class);
							menuActivity.putExtra("userId", jsonUser.getString("uid"));
							activitiy.startActivity(menuActivity);
						} else {
							Toast.makeText(activitiy, "Fehler bei Registrierung", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						Log.e("JSON Parser", "Error parsing data " + e.toString());
					}
				}
			}.execute(tag, pairUserName, pairEmail, pairUserPassword);
		}
		return userId;
	}

	public void checkUser(String name, String password) {
		BasicNameValuePair tag = new BasicNameValuePair("tag", "login");
		BasicNameValuePair userName = new BasicNameValuePair("name", name);
		BasicNameValuePair userPassword = new BasicNameValuePair("password", password);

		new JSONParser(activitiy, "Anmelden...") {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				this.dialog.dismiss();
				int userId = 0;
				Log.i("DAV", "AsyncTask ending");
				// try parse the string to a JSON object
				JSONObject jObj = null;
				try {
					jObj = new JSONObject(result);
					Log.i("DAV", "checkUser_success: " + jObj.getString("success"));
					Log.i("DAV", "checkUser_error: " + jObj.getString("error"));
					if (jObj.getString("success").equals("1")) {
						if (jObj.getString("success").equals("1")) {
							JSONObject jsonUser = jObj.getJSONObject("user");
							Log.i("DAV", "checkUser_uid: " + jsonUser.getInt("uid"));
							Log.i("DAV", "checkUser_name: " + jsonUser.getString("name"));
							Log.i("DAV", "checkUser_email: " + jsonUser.getString("email"));
							userId = jsonUser.getInt("uid");
							User u = new User(jsonUser.getInt("uid"), jsonUser.getString("email"), jsonUser.getString("name")); // User zum Speichern in lokale die Datenbank anlegen
							ObjectSet<User> dbresult = db.queryByExample(u);
							if (!dbresult.isEmpty()) { // Wenn es den Eintrag schon in der DB gibt wird er nur geupdated
								User found = dbresult.next();
								found.setUserId(jsonUser.getInt("uid"));
								found.setUserName(jsonUser.getString("name"));
								found.seteMail(jsonUser.getString("email"));
								db.store(found);
							} else { // Ansonsten wird ein neuer Eintrag angelegt
								db.store(u);
							}
						}
					} else {
						Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					Log.e("JSON Parser", "Error parsing data " + e.toString());
				}

				if (userId != 0) {
					Intent menuActivity = new Intent(activitiy.getApplicationContext(), MenuActivity.class);
					menuActivity.putExtra("userId", userId);

					SharedPreferences sp = activitiy.getSharedPreferences("Login", Context.MODE_PRIVATE);
					SharedPreferences.Editor ed = sp.edit();
					MainActivity test = (MainActivity) activitiy;

					if (test.checkBox.isChecked()) {
						ed.putString("Unm", test.username.getText().toString());
						ed.putString("Psw", test.password.getText().toString());
						ed.putBoolean("Chk", true);
					} else {
						ed.putString("Unm", null);
						ed.putString("Psw", null);
						ed.putBoolean("Chk", false);
					}
					ed.commit();
					activitiy.startActivity(menuActivity);
				}
			}
		}.execute(tag, userName, userPassword);
	}

	@SuppressLint("SimpleDateFormat")
	public Boolean setRouteRating(String rating, String howClimbed, String categorie, int userId, int routeId) {
		Boolean success = false;

		if (isOnline()) {
			// Rating in die lokale DB schreiben
			Route r = this.getRoute(new Route(routeId));
			Rating a = new Rating(rating, howClimbed, categorie, this.getUser(new User(userId)), r, true); // Rating zum Speichern in die Datenbank anlegen
			db.store(a); // Rating in der lokalen DB speichern
			r.setAverageRating(this.getAvarageRouteRating(r, rating));
			if (howClimbed.equals("Flash")) {
				r.setFlashCount(r.getFlashCount() + 1);
			} else if (howClimbed.equals("Rotpunkt")) {
				r.setRedpointCount(r.getRedpointCount() + 1);
			} else {
				r.setProjectCount(r.getProjectCount() + 1);
			}
			r.setRatingCount(r.getRatingCount() + 1);
			r.setPersonalRating(a);
			db.store(r);

			// Rating in die entfernte DB schreiben
			BasicNameValuePair tag = new BasicNameValuePair("tag", "setrating");
			BasicNameValuePair pairRouteId = new BasicNameValuePair("routeid", routeId + "");
			BasicNameValuePair pairUserId = new BasicNameValuePair("userid", userId + "");
			BasicNameValuePair pairCategorie = new BasicNameValuePair("categorie", categorie);
			BasicNameValuePair pairHowClimbed = new BasicNameValuePair("howclimbed", howClimbed);
			BasicNameValuePair pairRating = new BasicNameValuePair("rating", rating);

			new JSONParser(activitiy, "Bewertung abschicken...") {
				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					this.dialog.dismiss();
					JSONObject jObj = null;
					try {
						jObj = new JSONObject(result);
						Log.i("DAV", "setRouteRating_success: " + jObj.getString("success"));
						Log.i("DAV", "setRouteRating_error: " + jObj.getString("error"));
						if (jObj.getString("success").equals("0")) {
							Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						Log.e("JSON Parser", "Error parsing data " + e.toString());
					}
					activitiy.finish();
				}
			}.execute(tag, pairRouteId, pairUserId, pairCategorie, pairHowClimbed, pairRating);
		}
		return success;
	}

	public Boolean recoverPassword(String userName) {
		Boolean success = false;
		if (isOnline()) {
			BasicNameValuePair tag = new BasicNameValuePair("tag", "recoverpassword");
			BasicNameValuePair userNamePair = new BasicNameValuePair("name", userName);
			new JSONParser(activitiy, "Passwort zurücksetzten...") {
				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					// try parse the string to a JSON object
					this.dialog.dismiss();
					JSONObject jObj = null;
					try {
						jObj = new JSONObject(result);
						if (jObj.getString("success").equals("1")) {
							Toast.makeText(activitiy, "Passwort an ihre eMail Adresse gesendet", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						Log.e("JSON Parser", "Error parsing data " + e.toString());
					}
					SharedPreferences sp = activitiy.getApplication().getSharedPreferences("Login", Context.MODE_PRIVATE);
					SharedPreferences.Editor ed = sp.edit();
					ed.putBoolean("isPasswordResetted", true);
					ed.commit();
				}
			}.execute(tag, userNamePair);
		} else {
			Toast.makeText(activitiy, "Keine Internetverbindung vorhanden", Toast.LENGTH_LONG).show();
		}
		return success;
	}

	public Boolean setUserPassword(String userName, String password) {
		Boolean success = false;
		if (isOnline()) {
			BasicNameValuePair tag = new BasicNameValuePair("tag", "setuserpassword");
			BasicNameValuePair userNamePair = new BasicNameValuePair("name", userName);
			BasicNameValuePair userPasswordPair = new BasicNameValuePair("password", password);

			new JSONParser(activitiy, "Sende neues Passwort...") {
				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					this.dialog.dismiss();
					// try parse the string to a JSON object
					JSONObject jObj = null;
					try {
						jObj = new JSONObject(result);
						if (jObj.getString("success").equals("1")) {
							Toast.makeText(activitiy, "Neues Passwort wurde gesetzt", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(activitiy, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						Log.e("JSON Parser", "Error parsing data " + e.toString());
					}
				}
			}.execute(tag, userNamePair, userPasswordPair);
		} else {
			Toast.makeText(activitiy, "Keine Internetverbindung vorhanden", Toast.LENGTH_LONG).show();
		}
		return success;
	}

	private String getAvarageRouteRating(Route route, String rating) {
		List<Float> ratingList = new ArrayList<Float>();
		float ratingNumber = 0;
		float avarageRating = 0;
		String ratingAdd = null;
		for (int i = 0; i <= 1; i++) {
			if (rating.length() == 2) {
				ratingNumber = Integer.parseInt(rating.substring(0, 1));
				ratingAdd = rating.substring(1, 2);
				if (ratingAdd.equals("+"))
					ratingNumber += 0.3;
				else
					ratingNumber -= 0.3;
			} else if (rating.length() == 3) {
				ratingNumber = Integer.parseInt(rating.substring(0, 2));
				ratingAdd = rating.substring(3, 4);
				if (ratingAdd.equals("+"))
					ratingNumber += 0.3;
				else
					ratingNumber -= 0.3;
			} else {
				ratingNumber = Integer.parseInt(rating);
			}
			rating = route.getAverageRating();
			ratingList.add(ratingNumber);
		}
		Log.i("DAV", "Rating1: " + ratingList.get(0) + " Rating2: " + ratingList.get(1) + " RatingCount: " + route.getRatingCount());
		int ratingCount = route.getRatingCount() + 1;
		avarageRating = (ratingList.get(0) + (ratingList.get(1) * ratingCount)) / (ratingCount + 1);
		Log.i("DAV", "avarageRating: " + avarageRating);
		float y = avarageRating - (int) avarageRating;
		String newAvarageRating = null;
		if ((y >= 0) && (y <= 0.15))
			newAvarageRating = (int) (avarageRating - y) + "";
		else if ((y > 0.15) && (y <= 0.3))
			newAvarageRating = (int) (avarageRating - y) + "+";
		else if ((y > 0.3) && (y <= 0.5))
			newAvarageRating = (int) (avarageRating - y) + "+";
		else if ((y > 0.5) && (y <= 0.7))
			newAvarageRating = (int) (avarageRating - y + 1) + "-";
		else if ((y > 0.7) && (y <= 0.85))
			newAvarageRating = (int) (avarageRating - y + 1) + "-";
		else if ((y > 0.85) && (y <= 1))
			newAvarageRating = (int) (avarageRating - y + 1) + "";

		return newAvarageRating;
	}

	@SuppressWarnings("static-access")
	public void createNotification(int userId, int routeId) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(activitiy).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Neue Route")
				.setContentText("Es wurde eine neue Route hinzugefügt. Klicken um die Route anzuzeigen.");

		Intent notificationIntent = new Intent(activitiy, RouteDetailsActivity.class);
		notificationIntent.putExtra("routeId", routeId);
		notificationIntent.putExtra("userId", userId);
		PendingIntent contentIntent = PendingIntent.getActivity(activitiy, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);

		// Add as notification
		NotificationManager manager = (NotificationManager) activitiy.getSystemService(activitiy.NOTIFICATION_SERVICE);
		manager.notify(100, builder.build());
	}
}
