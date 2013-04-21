package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

	public Button login;
	public Button register;
	public Button recoverPassword;
	public EditText username;
	public EditText password;
	public CheckBox checkBox;
	private DBConnector db;
	private int userId;
	private final Context context = this;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		login = (Button) this.findViewById(R.id.loginButton);
		register = (Button) this.findViewById(R.id.registerButton);
		recoverPassword = (Button) this.findViewById(R.id.recoverPasswordButton);
		checkBox = (CheckBox) this.findViewById(R.id.loginCheckBox);
		username = (EditText) this.findViewById(R.id.emailText);
		password = (EditText) this.findViewById(R.id.passwordText);

		sp = getApplication().getSharedPreferences("Login", MODE_PRIVATE);
		username.setText(sp.getString("Unm", null));
		password.setText(sp.getString("Psw", null));
		checkBox.setChecked(sp.getBoolean("Chk", false));
		db = new DBConnector(this);

		new Thread() {
			@Override
			public void run() {
				
			}
		}.run();
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "Kein Benutzername oder Passwort eingegeben!", Toast.LENGTH_LONG).show();
				} else {
					if (db.isOnline()) {

								db.checkUser(username.getText().toString(), password.getText().toString());

					} else {
						Toast.makeText(getApplicationContext(), "Keine Internetverbindung vorhanden", Toast.LENGTH_LONG).show();
						/* Alert Dialog Code Start */
						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						alert.setTitle("Offline Modus?"); // Set Alert dialog title here
						alert.setMessage("Es besteht keine Internetverbindung. Im Offline Modus fortfahren?"); // Message here

						alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
								userId = db.getUser(new User(0, null, username.getText().toString())).getUserId();
								menuActivity.putExtra("userId", userId);
								menuActivity.putExtra("offline", true);
								startActivity(menuActivity);

							}
						});

						alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alert.create();
						alertDialog.show();
						/* Alert Dialog Code End */
					}
				}
			}
		});

		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (db.isOnline()) {
					Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
					startActivity(registerActivity);
				} else {
					Toast.makeText(getApplicationContext(), "Keine Internetverbindung vorhanden", Toast.LENGTH_LONG).show();
				}
			}
		});

		recoverPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* Alert Dialog Code Start */
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("Passwort vergessen?"); // Set Alert dialog title here
				alert.setMessage("Ihnen wird per eMail ein neues Passwort zugesendet. Wollen sie fortfahren?"); // Message here

				alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					@Override
					@SuppressLint("CommitPrefEdits")
					public void onClick(DialogInterface dialog, int whichButton) {
						db.recoverPassword(username.getText().toString());
					}
				});

				alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alert.create();
				alertDialog.show();
				/* Alert Dialog Code End */
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
