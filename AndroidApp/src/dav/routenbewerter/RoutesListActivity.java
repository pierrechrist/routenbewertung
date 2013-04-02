package dav.routenbewerter;

import java.util.ArrayList;
import java.util.List;

import com.dav.routenbewerter.R;
import com.db4o.ObjectSet;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RoutesListActivity extends ListActivity {

	private DBConnector db;
	private int userId;
	private String rating;
	private String wallName;
	private String categorie;
	private String howClimbed;
	private Boolean isOldestRoutes;
	private Boolean isNewestRoutes;
	private List<Route> result = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routelist);
		
		Intent i = getIntent();
		userId = i.getIntExtra("userId", 0);
		rating = i.getStringExtra("rating");
		wallName = i.getStringExtra("wallName");
		categorie = i.getStringExtra("categorie");
		howClimbed = i.getStringExtra("howClimbed");
		isNewestRoutes = i.getBooleanExtra("newestRoutes", false);
		isOldestRoutes = i.getBooleanExtra("oldestRoutes", false);
		
		result = new ArrayList<Route>();
		
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
        Log.i("DAV", "RouteId: " + result.get(position).routeNumber);
        routeDetailsActivity.putExtra("routeId", result.get(position).routeNumber);
        routeDetailsActivity.putExtra("userId", userId);
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
		db = new DBConnector(this);
		db.openDB();
		if(howClimbed != null) {
			Log.i("DAV", "howClimbed: " + howClimbed);
			ObjectSet<Rating> ratings = db.getRatings(new Rating(howClimbed, db.getUser(new User(userId))));
			Rating r = null;
			while(ratings.hasNext()) {
				r = ratings.next();
				Route route = r.getRoute();
				Boolean isCategorie = false;
				Boolean isRating = false;
				Boolean isWallName = false;
				if(categorie != null && route.getAvarageCategorie().equals(categorie)) {
					isCategorie = true;
				} else if(categorie == null) {
					isCategorie = true;
				}
				if(rating != null && route.getRating().equals(rating)) {
					isRating = true;
				} else if(rating == null) {
					isRating = true;
				}
				if(wallName != null && route.getWallName().equals(wallName)) {
					isWallName = true;
				} else if(wallName == null) {
					isWallName = true;
				}
				
				
				if(isCategorie && isRating && isWallName)
					result.add(route);
			}
		} else if(isNewestRoutes) {
			result = db.getNewsestRoutes();

		} else if(isOldestRoutes) {
			result = db.getOldestRoutes();
		} else {
			result = db.getRoutes(new Route(wallName, rating, categorie));
		}
		
		ListView list = getListView();
		ArrayAdapter<Route> adapter = new RouteAdapter(this, R.layout.route_list, result, db.getRatings(new Rating(new User(userId))));
	    list.setAdapter(adapter);
	}
}
