package dav.routenbewerter;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class DBInsert extends AsyncTask<String, Integer, Void> {
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("DAV", "DB Insert starting");
    }
	
	@Override
	protected Void doInBackground(String... sql) {	
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 //Wertepaar für $_Request anlegen
		 nameValuePairs.add(new BasicNameValuePair("sql",sql[0]));
		 
		 // http post
		 try {
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpPost httppost = new HttpPost("http://80.82.209.90/~web1/query.php");	//Adresse des PHP Scripts das auf die DB zugreift und JSON zurückliefert
			 httppost.setEntity (new UrlEncodedFormEntity(nameValuePairs));
			 HttpResponse response = httpclient.execute(httppost);
			 response.getEntity();
		 } catch ( Exception e){
			 Log.e("log_tag", "Error in http connection "+e.toString());
		 }        
		
		return null;
	}

	@Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.i("DAV", "DB Insert ending");   
    }
}
