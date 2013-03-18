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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MenuActivity extends Activity {

	private Button routeList;
	private Button personalStats;
	private Spinner rating;
	private CheckBox ratingLable;
	private Spinner categorie;
	private CheckBox categorieLable;
	private Spinner howClimbed;
	private CheckBox howClimbedLable;
	private EditText wallName;
	private CheckBox wallNameLable;
	private DBConnector db;
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		routeList = (Button) this.findViewById(R.id.menuRoutesButton);
		personalStats = (Button) this.findViewById(R.id.menuPersonalButton);
		
		wallName = (EditText) findViewById(R.id.filterWallName);
		rating = (Spinner) findViewById(R.id.filterRating);
		categorie = (Spinner) findViewById(R.id.filterCategorie);
		howClimbed = (Spinner) findViewById(R.id.filterHowClimbed);
		wallNameLable = (CheckBox) findViewById(R.id.filterWallNameLable);
		ratingLable = (CheckBox) findViewById(R.id.filterRatingLable);
		categorieLable = (CheckBox) findViewById(R.id.filterCategorieLable);
		howClimbedLable = (CheckBox) findViewById(R.id.filterHowClimbedLable);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ratingNumber, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rating.setAdapter(adapter);
		adapter = ArrayAdapter.createFromResource(this, R.array.categorie, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorie.setAdapter(adapter);
		adapter = ArrayAdapter.createFromResource(this, R.array.howClimbed, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		howClimbed.setAdapter(adapter);
		
		Intent i = getIntent();
        // Receiving the Data
		userId = i.getIntExtra("userId", 0);
		
		db = new DBConnector(this);
		db.openDB();
		
		if(userId != 0){
			db.syncDB(userId);
			User u = new User(userId, null, null);
			User uResult = db.getUser(u);
			Log.i("DAV", "UserName: " + u.getUserName());
			Toast.makeText(getApplicationContext(), "UserId: "+uResult.getUserId()+" UserName: "+uResult.getUserName(), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "Offline Modus", Toast.LENGTH_LONG).show();
		}  
		
		routeList.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent routesListActivity = new Intent(getApplicationContext(), RoutesListActivity.class);
	              routesListActivity.putExtra("userId", userId);
	              if(wallNameLable.isChecked())
	            	  routesListActivity.putExtra("wallName", wallName.getText().toString());
	              if(ratingLable.isChecked())
	            	  routesListActivity.putExtra("rating", rating.getSelectedItem().toString());
	              if(categorieLable.isChecked())
	            	  routesListActivity.putExtra("categorie", categorie.getSelectedItem().toString());
	              if(howClimbedLable.isChecked())
	            	  routesListActivity.putExtra("howClimbed", howClimbed.getSelectedItem().toString());
	              
	              startActivity(routesListActivity);
	          }
	        });
	        
	        
		personalStats.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent personalDetailsActivity = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
	              personalDetailsActivity.putExtra("userId", userId);
	              startActivity(personalDetailsActivity);
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
	
	@Override
	protected void onResume() {
		super.onResume();
	}

}
