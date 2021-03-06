package dav.routenbewerter;

import java.util.Date;

import com.dav.routenbewerter.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MenuActivity extends Activity {

	private Button routeList;
	private Button personalStats;
	private Button qrCodeScanner;
	private Spinner rating;
	private TextView ratingLable;
	private Spinner categorie;
	private TextView categorieLable;
	private Spinner howClimbed;
	private TextView howClimbedLable;
	private Spinner wallName;
	private TextView wallNameLable;
	private RadioGroup radioGroup;
	private DBConnector db;
	private int userId;
	private Activity activity;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		activity = this;
		
		sp = getApplication().getSharedPreferences("Login", MODE_PRIVATE);
		
		routeList = (Button) this.findViewById(R.id.menuRoutesButton);
		personalStats = (Button) this.findViewById(R.id.menuPersonalButton);
		
		wallName = (Spinner) findViewById(R.id.filterWallName);
		rating = (Spinner) findViewById(R.id.filterRating);
		categorie = (Spinner) findViewById(R.id.filterCategorie);
		howClimbed = (Spinner) findViewById(R.id.filterHowClimbed);
		wallNameLable = (TextView) findViewById(R.id.filterWallNameLable);
		ratingLable = (TextView) findViewById(R.id.filterRatingLable);
		categorieLable = (TextView) findViewById(R.id.filterCategorieLable);
		howClimbedLable = (TextView) findViewById(R.id.filterHowClimbedLable);
		qrCodeScanner = (Button) findViewById(R.id.menuQRCodeButton);
		radioGroup = (RadioGroup) findViewById(R.id.menuRadioGroup);

		radioGroup.check(R.id.menuNoFilter);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filterNumber, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rating.setAdapter(adapter);
		adapter = ArrayAdapter.createFromResource(this, R.array.filterCategorie, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorie.setAdapter(adapter);
		adapter = ArrayAdapter.createFromResource(this, R.array.filterHowClimbed, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		howClimbed.setAdapter(adapter);
		adapter = ArrayAdapter.createFromResource(this, R.array.filterWallName, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wallName.setAdapter(adapter);
		
		Intent i = getIntent();
        // Receiving the Data
		userId = i.getIntExtra("userId", 0);
		
		
		db = new DBConnector(this);
		db.openDB();
		
		User u = new User(userId, null, null);
		User uResult = db.getUser(u);
		
		if(!i.getBooleanExtra("offline", false)){
			//Testen ob seit der letzten Sync 6 Stunden vergangen sind
			Date original = new Date(sp.getLong("syncDate", 0));
			Long now = new Date().getTime();
			Date minus6 = new Date(now - 6*3600*1000);
			if (original.before(minus6)) {
				db.syncDB(userId);
			} 
		}  
		
		if(sp.getBoolean("isPasswordResetted", false)) {
			this.setUserPasswordDialog(uResult.getUserName());
		}
		
		routeList.setOnClickListener(new OnClickListener()
	        {
	          @Override
			public void onClick(View v)
	          {
	              Intent routesListActivity = new Intent(getApplicationContext(), RoutesListActivity.class);
	              routesListActivity.putExtra("userId", userId);
	              if(radioGroup.getCheckedRadioButtonId() == R.id.menuRouteFilter) {
		              if(!wallName.getSelectedItem().toString().equals(""))
		            	  routesListActivity.putExtra("wallName", wallName.getSelectedItem().toString());
		              if(!rating.getSelectedItem().toString().equals(""))
		            	  routesListActivity.putExtra("rating", rating.getSelectedItem().toString());
		              if(!categorie.getSelectedItem().toString().equals(""))
		            	  routesListActivity.putExtra("categorie", categorie.getSelectedItem().toString());
		              if(!howClimbed.getSelectedItem().toString().equals(""))
		            	  routesListActivity.putExtra("howClimbed", howClimbed.getSelectedItem().toString());
	              } else if(radioGroup.getCheckedRadioButtonId() == R.id.menuNewestRouteFilter) {
	            	  routesListActivity.putExtra("newestRoutes", true);
	              } else if(radioGroup.getCheckedRadioButtonId() == R.id.menuOldestRouteFilter) {
	            	  routesListActivity.putExtra("oldestRoutes", true);
	              }
	              
	              startActivity(routesListActivity);
	          }
	        });
	        
	        
		personalStats.setOnClickListener(new OnClickListener()
	        {
	          @Override
			public void onClick(View v)
	          {
	              Intent personalDetailsActivity = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
	              personalDetailsActivity.putExtra("userId", userId);
	              startActivity(personalDetailsActivity);
	          }
	        });
		
		qrCodeScanner.setOnClickListener(new OnClickListener()
        {
          @Override
		public void onClick(View v)
          {
        	  IntentIntegrator integrator = new IntentIntegrator(activity);
        	  integrator.initiateScan();
          }
        });
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
                if(checkedId == R.id.menuRouteFilter) {
                	rating.setVisibility(View.VISIBLE);
                	ratingLable.setVisibility(View.VISIBLE);
                	categorie.setVisibility(View.VISIBLE);
                	categorieLable.setVisibility(View.VISIBLE);
                	howClimbed.setVisibility(View.VISIBLE);
                	howClimbedLable.setVisibility(View.VISIBLE);
                	wallName.setVisibility(View.VISIBLE);
                	wallNameLable.setVisibility(View.VISIBLE);
                } else {
                	rating.setVisibility(View.GONE);
                	ratingLable.setVisibility(View.GONE);
                	categorie.setVisibility(View.GONE);
                	categorieLable.setVisibility(View.GONE);
                	howClimbed.setVisibility(View.GONE);
                	howClimbedLable.setVisibility(View.GONE);
                	wallName.setVisibility(View.GONE);
                	wallNameLable.setVisibility(View.GONE);
                }
                	
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.mDbSync:
	        	if(!db.isDbOpen())
	        		db.openDB();
	        	db.syncDB(userId);
	            return true;
	        case R.id.mPersonalStats:
	        	Intent personalDetailsActivity = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
	            personalDetailsActivity.putExtra("userId", userId);
	            startActivity(personalDetailsActivity);
	            return true;
	        case R.id.mPasswordChange:
	        	if(!db.isDbOpen()) {
	        		db.openDB();
	        	}
	        	User u = new User(userId, null, null);
	    		User uResult = db.getUser(u);
	        	this.setUserPasswordDialog(uResult.getUserName());
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = null;
		if(requestCode != 0 && resultCode != 0 && intent != null) {
		  scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		}
		  if (scanResult != null) {
		    Log.i("DAV", "QR Result: "+scanResult.getContents());
		    if(scanResult.getContents().startsWith("DAV")){
		    	Intent routeDetailsActivity = new Intent(getApplicationContext(), RouteDetailsActivity.class);
		        routeDetailsActivity.putExtra("routeId", Integer.parseInt(scanResult.getContents().substring(4)));
		        Log.i("DAV", "QR Result: "+Integer.parseInt(scanResult.getContents().substring(4)));
		        routeDetailsActivity.putExtra("userId", userId);
		        startActivity(routeDetailsActivity);
		    } else {
				Toast.makeText(getApplicationContext(), "Keine Route in diesem QR-Code gefunden", Toast.LENGTH_LONG).show();
			}
		  } else {
			  Toast.makeText(getApplicationContext(), "QR-Code Scann brachte kein Ergebniss", Toast.LENGTH_LONG).show();
		  }
	}
	
	private void setUserPasswordDialog(final String userName) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Passwort zur�ckgesetzt");
		alert.setMessage("Bitte geben sie ein neues Passwort ein:");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				db.setUserPassword(userName, input.getText().toString());
				SharedPreferences.Editor ed = sp.edit();
				ed.putBoolean("isPasswordResetted", false);
				ed.commit();
			}
		});

		alert.setNegativeButton("Abbrechen",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});

		alert.show();
	}

}
