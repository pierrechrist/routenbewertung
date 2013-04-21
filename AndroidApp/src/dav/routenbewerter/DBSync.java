package dav.routenbewerter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dav.routenbewerter.R;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class DBSync extends AsyncTask<Integer, Integer, String> {

	private ProgressDialog dialog;
	private DBConnector connector;
	private ObjectContainer db;
	private InputStream is = null;
	private ArrayList<NameValuePair> nameValuePairs = null;
	private Date lastSync;
	private String url;

	class changeMessage implements Runnable {
		String str;
		ProgressDialog dialog;

		changeMessage(String s, ProgressDialog dialog) {
			str = s;
			this.dialog = dialog;
		}

		public void run() {
			this.dialog.setMessage(str);
		}
	}

	public DBSync(Activity activity, DBConnector connector, ObjectContainer db) {
		dialog = new ProgressDialog(activity);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.setCancelable(false);
		SharedPreferences sp = activity.getApplication().getSharedPreferences("Login", Context.MODE_PRIVATE);
		url = activity.getResources().getString(R.string.api_url);
		lastSync = new Date(sp.getLong("syncDate", 0));
		Log.i("DAV","lastSync 0"+lastSync.getTime()+"");
		this.connector = connector;
		this.db = db;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.i("DAV", "AsyncTask starting");
		this.dialog.setTitle("DB Sync...");
		this.dialog.setMessage("DB Sync...");
		if (!this.dialog.isShowing()) {
			this.dialog.show();
		}
	}

	@Override
	protected String doInBackground(Integer... params) {
		String result = "";
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("tag", "getroutes"));
		if(lastSync.getTime() == 00) {
			nameValuePairs.add(new BasicNameValuePair("timestamp", "0"));
		} else {
			nameValuePairs.add(new BasicNameValuePair("timestamp", (lastSync.getTime()+"").substring(0, 10)));
		}
		result = this.httpPost(nameValuePairs);
		
		this.setRoutes(result, params[0]);
		
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("tag", "getroutes"));
		result = this.httpPost(nameValuePairs);
		
		this.deleteRoutes(result);

		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("tag", "getratings"));
		nameValuePairs.add(new BasicNameValuePair("userid", params[0] + ""));

		result = this.httpPost(nameValuePairs);

		this.setRatings(result, params[0]);

		this.sendRatings();

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		this.dialog.dismiss();
	}

	private String httpPost(ArrayList<NameValuePair> nameValuePairs) {
		dialog.setProgress(0);
		dialog.setMax(2);
		connector.activitiy.runOnUiThread((Runnable) new changeMessage("Lade Daten aus dem Web....", dialog));
		String result = "";
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url); // Adresse des PHP Scripts das auf die DB zugreift und JSON zurückliefert
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		dialog.setProgress(1);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		dialog.setProgress(2);
		return result;
	}

	private void sendRatings() {
		// nicht abgesendete Ratings an entfernte DB schicken
		Query query = db.query();
		query.constrain(Rating.class);
		query.descend("sent").constrain(false);

		ObjectSet<Rating> res = query.execute();
		if (res.size() > 0) {
			connector.activitiy.runOnUiThread((Runnable) new changeMessage("Sende nicht abgechickte Routen", dialog));
			dialog.setProgress(0);
			dialog.setMax(res.size());
		}
		while (res.hasNext()) {
			Rating r = res.next();
			// Rating in die entfernte DB schreiben
			nameValuePairs.add(new BasicNameValuePair("tag", "setrating"));
			nameValuePairs.add(new BasicNameValuePair("routeid", r.getRoute().getRouteNumber() + ""));
			nameValuePairs.add(new BasicNameValuePair("userid", r.getUser().getUserId() + ""));
			nameValuePairs.add(new BasicNameValuePair("categorie", r.getCategorie()));
			nameValuePairs.add(new BasicNameValuePair("howclimbed", r.getHowClimbed()));
			nameValuePairs.add(new BasicNameValuePair("rating", r.getRating()));
			this.httpPost(nameValuePairs);
			Log.i("DAV", "Offline Rating abgeschickt: " + r.getRoute().getRouteNumber());
			r.setSent(true);
			db.store(r);
			dialog.setProgress(dialog.getProgress() + 1);
		}
	}

	private void setRoutes(String result, int userId) {
		JSONObject jObj;
		try {
			jObj = new JSONObject(result);
			Log.i("DAV", "routeSnyc_success: " + jObj.getString("success"));
			Log.i("DAV", "routeSnyc_error: " + jObj.getString("error"));
			JSONArray jArray = jObj.getJSONArray("route");
			int i;
			connector.activitiy.runOnUiThread((Runnable) new changeMessage("Schreibe Routen in die lokale DB", dialog));
			dialog.setProgress(0);
			dialog.setMax(jArray.length());
			for (i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);

				// db4o
				Route r = new Route(json_data.getInt("uid"));
				ObjectSet<Route> dbresult = db.queryByExample(r);

				String rating = null;
				if (json_data.getString("rating").equals("null"))
					rating = json_data.getString("uiaa");
				else
					rating = json_data.getString("rating");

				if (!dbresult.isEmpty()) {
					Route found = dbresult.next();
					if (!found.equals(r)) {
						found.setRating(rating);
						found.setAverageRating(json_data.getString("avarage_rating"));
						found.setAvarageCategorie(json_data.getString("avarage_categorie"));
						found.setFlashCount(json_data.getInt("flash_count"));
						found.setRedpointCount(json_data.getInt("redpoint_count"));
						db.store(found);
					}
				} else {
					r = new Route(json_data.getInt("uid"), json_data.getString("color"), json_data.getString("createdby"), json_data.getString("sektor"), json_data.getInt("dateon"),
							json_data.getInt("tr"), json_data.getInt("boltrow"), rating, json_data.getString("avarage_rating"), json_data.getInt("rating_count"), json_data.getString("avarage_categorie"),
							json_data.getInt("flash_count"), json_data.getInt("redpoint_count"), json_data.getInt("project_count"));
					db.store(r);
					connector.createNotification(userId, r.getRouteNumber());
				}
				dialog.setProgress(i);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteRoutes(String result) {
		/* TODO Nichtmehr vorhandene Routen löschen */
		JSONObject jObj;
		try {
			jObj = new JSONObject(result);
			JSONArray jArray = jObj.getJSONArray("route");
			ObjectSet<Route> r = connector.getRoutes();	
			Log.i("DAV", "Mehr Routen in lokaler DB als in entfernter DB");
			while (r.hasNext()) {
				Boolean found = false;
				Route route = r.next();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					if (route.getRouteNumber() == json_data.getInt("uid"))
						found = true;
				}
				if (!found) {
					Rating rating = connector.getRating(new Rating(route, null));
					db.delete(route);
					if (rating != null)
						db.delete(rating);
					Log.i("DAV", "Route gelöscht: " + route.getRouteNumber());
				}
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private void setRatings(String result, int userId) {
		try {
			JSONObject jObj = new JSONObject(result);
			Log.i("DAV", "ratingSnyc_success: " + jObj.getString("success"));
			Log.i("DAV", "ratingSnyc_error: " + jObj.getString("error"));
			JSONArray jArray = jObj.getJSONArray("rating");
			if (connector.getRatings(new Rating(new User(userId))).size() < jArray.length()) {
				connector.activitiy.runOnUiThread((Runnable) new changeMessage("Schreibe Ratings in die lokale DB", dialog));
				dialog.setProgress(0);
				dialog.setMax(jArray.length());
				for (int i = 0; i < jArray.length(); i++) {
					
					JSONObject json_data = jArray.getJSONObject(i);

					// db4o
					Rating a = new Rating(connector.getRoute(new Route(json_data.getInt("route_id"))), connector.getUser(new User(json_data.getInt("user_id"))));
					ObjectSet<Rating> dbresult = db.queryByExample(a);

					if (dbresult.isEmpty()) {
						SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = null;
						try {
							date = sdfToDate.parse(json_data.getString("crdate"));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Route route = connector.getRoute(new Route(json_data.getInt("route_id")));
						if (route != null) {
							a = new Rating(json_data.getString("uiaa"), json_data.getString("howclimbed"), json_data.getString("categorie"), date, connector.getUser(new User(json_data
									.getInt("user_id"))), route, true);
							route.setPersonalRating(a);
							db.store(a);
							db.store(route);
						}
					} 				
				dialog.setProgress(i);
				}
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
	}
}
