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

import android.os.AsyncTask;
import android.util.Log;

public class DBCheckLogin extends AsyncTask<String, Integer, Boolean> {
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("DAV", "LoginCheck starting");
    }
	
	@Override
	protected Boolean doInBackground(String... params) {	
		 String result = "";
		 InputStream is = null;
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 //Wertepaar für $_Request anlegen
		 nameValuePairs.add(new BasicNameValuePair("sql","SELECT `user_name`, `user_password` FROM `rb_user` WHERE `user_name` = '"+params[0]+"' AND `user_password` = '"+params[1]+"'"));
		 
		 try {
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpPost httppost = new HttpPost("http://80.82.209.90/~web1/query.php");	//Adresse des PHP Scripts das auf die DB zugreift und JSON zurückliefert
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
		Boolean returnResult = false;
		try {
		    JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);
			
			if(params[0].equals(json_data.getString("user_name")) && params[1].equals(json_data.getString("user_password")))
				returnResult = true;
			else
				returnResult = false;

		} catch (JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
		}      
		return returnResult;
	}

	@Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.i("DAV", "LoginCheck ending");   
    }
}
