package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RateRouteActivity extends Activity {

	private Button accept;
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rateroute);
		
		accept = (Button) this.findViewById(R.id.raterouteAcceptButton);
		cancel = (Button) this.findViewById(R.id.raterouteCancelButton);
	        
		accept.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	        	  finish();
	          }
	        });
	        
	        
		cancel.setOnClickListener(new OnClickListener()
	        {
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

}
