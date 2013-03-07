package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button login;
    private Button register;
    private Button offline;
    private EditText username;
    private EditText password;
    private CheckBox checkBox;
    private DBConnector db;
    private int userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		login = (Button) this.findViewById(R.id.loginButton);
		register = (Button) this.findViewById(R.id.registerButton);
		offline = (Button) this.findViewById(R.id.offlineButton);
		checkBox = (CheckBox) this.findViewById(R.id.loginCheckBox);
		username = (EditText) this.findViewById(R.id.emailText);
		password = (EditText) this.findViewById(R.id.passwordText);
		
		SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
		username.setText(sp.getString("Unm", null));
		password.setText(sp.getString("Psw", null));
		checkBox.setChecked(sp.getBoolean("Chk", false));
		
		db = new DBConnector(this);
		
		login.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	        	  if(username.getText().toString().equals("") || password.getText().toString().equals("")) {
	        		  Toast.makeText(getApplicationContext(), "Kein Benutzername oder Passwort eingegeben!", Toast.LENGTH_LONG).show();
	        	  } else {
		        	  userId = db.checkUser(username.getText().toString(), password.getText().toString());
		        	  if(userId != 0) {
			              Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
			              menuActivity.putExtra("userId", userId);
			              
			              SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
			              SharedPreferences.Editor ed = sp.edit();
			              if(checkBox.isChecked()) {
			            	  ed.putString("Unm",username.getText().toString());              
			            	  ed.putString("Psw",password.getText().toString()); 
			            	  ed.putBoolean("Chk", true);
			              } else {
			            	  ed.putString("Unm",null);              
				              ed.putString("Psw",null);
				              ed.putBoolean("Chk", false);
			              }
			              ed.commit();		           		              
			              startActivity(menuActivity);
		        	  } 
	        	  }
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
		
		offline.setOnClickListener(new OnClickListener()
        {
          public void onClick(View v)
          {
              Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
              menuActivity.putExtra("userId", 0);
              startActivity(menuActivity);
          }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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
		db.openDB();
	}
	
	

}
