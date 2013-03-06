package dav.routenbewerter;

import com.dav.routenbewerter.R;
import com.db4o.ObjectSet;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RoutesListActivity extends ListActivity {

	private DBConnector db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routelist);
		
		ObjectSet<Route> result = null;
		db = new DBConnector(this);
		db.openDB();
		result = db.getRoutes();
		
		ListView list = getListView();
		ArrayAdapter<Route> adapter = new RouteAdapter(this, R.layout.route_list, result);
	    list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
        Intent routeDetailsActivity = new Intent(getApplicationContext(), RouteDetailsActivity.class);
        startActivity(routeDetailsActivity);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.closeDB();
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.closeDB();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

}
