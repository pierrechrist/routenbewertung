package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RateRouteActivity extends Activity {

	private Button accept;
	private Button cancel;
	private int userId;
	private int routeId;
	private DBConnector db;
	private Spinner rating;
	private Spinner categorie;
	private Spinner howClimbed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rateroute);
		
		accept = (Button) this.findViewById(R.id.raterouteAcceptButton);
		cancel = (Button) this.findViewById(R.id.raterouteCancelButton);
		
		Intent i = getIntent();
		userId = i.getIntExtra("userId", 0);
		routeId = i.getIntExtra("routeId", 0);
		
		rating = (Spinner) findViewById(R.id.raterouteRating1);
		categorie = (Spinner) findViewById(R.id.raterouteCategorie);
		howClimbed = (Spinner) findViewById(R.id.raterouteClimbed);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ratingNumber, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rating.setAdapter(adapter);
		adapter = ArrayAdapter.createFromResource(this, R.array.categorie, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorie.setAdapter(adapter);
		adapter = ArrayAdapter.createFromResource(this, R.array.howClimbed, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		howClimbed.setAdapter(adapter);
	        
		db = new DBConnector(this);
		db.openDB();
		
		accept.setOnClickListener(new OnClickListener()
	        {
	          @Override
			public void onClick(View v)
	          {
	        	  Log.i("DAV", "Rating: " + rating.getSelectedItem().toString());
	        	  db.setRouteRating(rating.getSelectedItem().toString(), howClimbed.getSelectedItem().toString(), categorie.getSelectedItem().toString(), userId, routeId);
	        	  finish();
	          }
	        });
	        
	        
		cancel.setOnClickListener(new OnClickListener()
	        {
	          @Override
			public void onClick(View v)
	          {
	        	 finish();
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
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.closeDB();
	}

}
