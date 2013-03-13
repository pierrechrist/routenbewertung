package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class PersonalDetailsActivity extends Activity {
	
	private int userId;
	private DBConnector db;
	private TextView userName;
	private TextView routeCount;
	private TextView flashCount;
	private TextView redPointCount;
	private TextView notClimbedCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personaldetails);
		
		userName = (TextView)this.findViewById(R.id.personalUsername);
		routeCount = (TextView)this.findViewById(R.id.personalRoutecount);
		flashCount = (TextView)this.findViewById(R.id.personalFlashcount);
		redPointCount = (TextView)this.findViewById(R.id.personalRotpunktcount);
		notClimbedCount = (TextView)this.findViewById(R.id.personalNotclimbedcount);
		
		Intent i = getIntent();
		userId = i.getIntExtra("userId", 0);
		
		db = new DBConnector(this);
		db.openDB();
		
		User u = new User(userId, null, null);
		u = db.getUser(u);
		
		Rating a = new Rating(null, u);
		a.setHowClimbed("Flash");
		int flashC = db.getRatings(a).size();
		a.setHowClimbed("Rotpunkt");
		int redpointC = db.getRatings(a).size();
		int routeC = db.getRoutes().size();
		
		userName.setText(u.getUserName());
		routeCount.setText(Integer.toString(routeC));
		flashCount.setText(Integer.toString(flashC));
		redPointCount.setText(Integer.toString(redpointC));
		notClimbedCount.setText(Integer.toString(routeC-(flashC+redpointC)));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.closeDB();
		Log.i("DAV", "DB Geschlossen. Activity Destroyed");
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.closeDB();
	}
	
}
