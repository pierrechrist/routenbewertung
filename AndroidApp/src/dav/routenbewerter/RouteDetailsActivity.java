package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RouteDetailsActivity extends Activity {

	private Button rate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routedetails);
		
		rate = (Button) this.findViewById(R.id.raterouteButton);
	        
		rate.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent rateRouteActivity = new Intent(getApplicationContext(), RateRouteActivity.class);

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

}
