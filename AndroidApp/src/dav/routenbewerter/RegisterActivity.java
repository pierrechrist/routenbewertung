package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterActivity extends Activity {

	private Button accept;
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		accept = (Button) this.findViewById(R.id.registerAcceptButton);
		cancel = (Button) this.findViewById(R.id.registerCancelButton);
	        
		accept.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);

	              startActivity(menuActivity);
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
