package dav.routenbewerter;
import com.db4o.ObjectContainer;
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
		return null;
	}

	@Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.i("DAV", "DB Sync ending");   
        db.close();
    }
}
