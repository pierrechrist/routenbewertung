package dav.routenbewerter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import android.os.AsyncTask;
import android.util.Log;

public class DBSync extends AsyncTask<ObjectContainer, Integer, Void> {

	private ObjectContainer db;
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("DAV", "DB Sync starting");
    }
	
	@Override
	protected Void doInBackground(ObjectContainer... params) {	
		 db = params[0];
		 String result = "";
		 InputStream is = null;
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 //Wertepaar f�r $_Request anlegen
		 nameValuePairs.add(new BasicNameValuePair("sql","SELECT r.uid, u.uiaa, r.color, r.dateon, r.createdby, s.sektor FROM tx_dihlroutes_routelist r LEFT JOIN tx_dihlroutes_uiaa u ON r.uiaa = u.uid LEFT JOIN tx_dihlroutes_sektor s ON r.sektor = s.uid WHERE r.deleted = '0'"));
		 
		 // http post
		 try {
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpPost httppost = new HttpPost("http://80.82.209.90/~web1/query.php");	//Adresse des PHP Scripts das auf die DB zugreift und JSON zur�ckliefert
			 httppost.setEntity (new UrlEncodedFormEntity(nameValuePairs));
			 HttpResponse response = httpclient.execute(httppost);
			 HttpEntity entity = response.getEntity();
			 is = entity.getContent();
		 } catch ( Exception e){
			 Log .e("log_tag", "Error in http connection "+e.toString());
		 }        
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
		
		//JSON Daten parsen
		try {
		    JSONArray jArray = new JSONArray(result);
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
		
		return null;
	}

	@Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.i("DAV", "DB Sync ending");   
        db.close();
    }
}