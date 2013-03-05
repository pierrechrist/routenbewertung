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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class JSONParser extends AsyncTask<BasicNameValuePair, Integer, String> {
	
	private ProgressDialog dialog;
	
	public JSONParser(Activity activity) {
        dialog = new ProgressDialog(activity);
    }
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("DAV", "AsyncTask starting");
        this.dialog.setMessage("Hole Daten aus dem Web");
        if(!this.dialog.isShowing()){
            this.dialog.show();
        }
    }
	
	@Override
	protected String doInBackground(BasicNameValuePair... params) {	
		 String result = "";
		 InputStream is = null; 
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 for(int i=0; i < params.length; i++) {
			 nameValuePairs.add(params[i]);
		 }
		 // http post
		 try {
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpPost httppost = new HttpPost("http://80.82.209.90/~web1/AndroidWebAPI/index.php");	//Adresse des PHP Scripts das auf die DB zugreift und JSON zurückliefert
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
		
		return result;
	}

	@Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);   
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Log.i("DAV", "AsyncTask ending");
    }
}
