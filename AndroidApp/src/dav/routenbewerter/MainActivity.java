package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button login;
    private Button register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		login = (Button) this.findViewById(R.id.loginButton);
		register = (Button) this.findViewById(R.id.registerButton);
	        
		login.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	        	 /*try {
					Boolean test = new DBCheckLogin().execute("Pierre", "test").get();
					Log.i("DAV", test.toString());
					test = new DBCheckLogin().execute("Hans", "test").get();
					Log.i("DAV", test.toString());
					test = new DBCheckLogin().execute("Pierre", "nix").get();
					Log.i("DAV", test.toString());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	              Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);

	              startActivity(menuActivity);
	          }
	        });
	        
	        
		register.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
	              
	              startActivity(registerActivity);
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
