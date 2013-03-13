package dav.routenbewerter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RouteDetailsActivity extends Activity {

	private DBConnector db;
	private int userId;
	private Button rate;
	private int routeId;
	private TextView routeNumber;
	private TextView wallName;
	private TextView routeDriver;
	private TextView creationDate;
	private TextView handleColor;
	private TextView rating;
	private TextView categorie;
	private TextView flash;
	private TextView redPoint;
	private TextView notClimbed;
	private TextView ownRating;
	private TextView ownHowClimbed;
	private TextView ownCategorie;
	private TextView ownRatingLable;
	private TextView ownHowClimbedLable;
	private TextView ownCategorieLable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routedetails);
		
		rate = (Button) this.findViewById(R.id.raterouteButton);
		routeNumber = (TextView)this.findViewById(R.id.routedetailsNumber);
		wallName = (TextView)this.findViewById(R.id.routedetailsWallname);
		routeDriver = (TextView)this.findViewById(R.id.routedetailsRoutedriver);
		creationDate = (TextView)this.findViewById(R.id.routedetailsCreationdate);
		handleColor = (TextView)this.findViewById(R.id.routedetailsHandlecolor);
		rating = (TextView)this.findViewById(R.id.routedetailsAvaragerating);
		categorie = (TextView)this.findViewById(R.id.routedetailsAvaragecategorie);
		flash = (TextView)this.findViewById(R.id.routedetailsFlashcount);
		redPoint = (TextView)this.findViewById(R.id.routedetailsRotpunktcount);
		notClimbed = (TextView)this.findViewById(R.id.routedetailsNotclimbedcount);
		ownRating = (TextView)this.findViewById(R.id.routedetailsOwnRating);
		ownHowClimbed = (TextView)this.findViewById(R.id.routedetailsOwnHowClimbed);
		ownCategorie = (TextView)this.findViewById(R.id.routedetailsOwnCategorie);
		ownRatingLable = (TextView)this.findViewById(R.id.routedetailsOwnRatingLable);
		ownHowClimbedLable = (TextView)this.findViewById(R.id.routedetailsOwnHowClimbedLable);
		ownCategorieLable = (TextView)this.findViewById(R.id.routedetailsOwnCategorieLable);
		
		Intent i = getIntent();
		routeId = i.getIntExtra("routeId", 0);
		userId = i.getIntExtra("userId", 0);
		
		rate.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent rateRouteActivity = new Intent(getApplicationContext(), RateRouteActivity.class);
	              rateRouteActivity.putExtra("userId", userId);
	              rateRouteActivity.putExtra("routeId", routeId);
	              startActivity(rateRouteActivity);
	          }
	        });
	        
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
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onResume() {
		super.onResume();
		db = new DBConnector(this);
		db.openDB();
		
		Route r = new Route(routeId);
		r = db.getRoute(r);
		
		String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(r.getCreationDate() * 1000L));		
		
		routeNumber.setText(Integer.toString(r.getRouteNumber()));
		wallName.setText(r.getWallName());
		routeDriver.setText(r.getRouteDriver());
		creationDate.setText(date);
		handleColor.setText(r.getHandleColor());
		rating.setText(r.getAverageRating());
		categorie.setText(r.getAvarageCategorie());
		flash.setText(Integer.toString(r.getFlashCount()));
		redPoint.setText(Integer.toString(r.getRedpointCount()));
		notClimbed.setText(Integer.toString(r.getNotClimbedCount()));
	        
		Rating a = new Rating(r, new User(userId));
		a = db.getRating(a);
		if(a != null) {
			Log.i("DAV", "Rating: "+ a.getRating());
			Log.i("DAV", "not null");
			rate.setVisibility(View.GONE);
			ownRatingLable.setVisibility(View.VISIBLE);
			ownHowClimbedLable.setVisibility(View.VISIBLE);
			ownCategorieLable.setVisibility(View.VISIBLE);
			ownRating.setVisibility(View.VISIBLE);
			ownHowClimbed.setVisibility(View.VISIBLE);
			ownCategorie.setVisibility(View.VISIBLE);
			ownRating.setText(a.getRating());
			ownHowClimbed.setText(a.getHowClimbed());
			ownCategorie.setText(a.getCategorie());
			
		} else
			Log.i("DAV", "null");
	}

}
