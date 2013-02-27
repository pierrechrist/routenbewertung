package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RoutesListActivity extends ListActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routelist);
		
		String[] values = new String[] { "Mount Everest", "Qogir", "Kanchenjunga",
		  "Lhotse", "Makalu I", "Cho Oyu", "Dhaulagiri", "Manaslu I",
		  "Nanga Parbat", "Annapurna I" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

		setListAdapter(adapter); 
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

}
