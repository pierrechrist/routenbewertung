package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private Button accept;
	private Button cancel;
	private EditText username;
	private EditText email;
	private EditText password1;
	private EditText password2;
	private DBConnector db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		accept = (Button) this.findViewById(R.id.registerAcceptButton);
		cancel = (Button) this.findViewById(R.id.registerCancelButton);
		username = (EditText) this.findViewById(R.id.registerUsernameText);
		email = (EditText) this.findViewById(R.id.registerEmailText);
		password1 = (EditText) this.findViewById(R.id.registerPasswordText1);
		password2 = (EditText) this.findViewById(R.id.registerPasswordText2);
	        
		db = new DBConnector(this);
		db.openDB();
		
		accept.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	        	  if(password1.getText().toString().equals(password2.getText().toString())) {
	        		  Boolean check = db.registerUser(email.getText().toString(), username.getText().toString(), password1.getText().toString());
	        		  if(check) {
	        			  Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
	        			  startActivity(menuActivity);
	        		  }
	        	  } else {
	        		  Toast.makeText(getApplicationContext(), "Passwörter nicht identisch!", Toast.LENGTH_LONG).show();
	        	  }
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
