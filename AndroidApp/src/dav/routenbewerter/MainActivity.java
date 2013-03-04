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
    private DBConnector db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		login = (Button) this.findViewById(R.id.loginButton);
		register = (Button) this.findViewById(R.id.registerButton);
		//db = new DBConnector(this);
		//db.registerUser("pierre@web.de", "peter", "hallo");
		
		login.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	        	  //db.checkUser("pierre", "test");
	        	  //db.syncDatabase();
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
