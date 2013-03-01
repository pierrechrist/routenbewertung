package dav.routenbewerter;

import com.dav.routenbewerter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {

	private Button routeList;
	private Button personalStats;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		//DBConnector db = new DBConnector(this);
		//db.mysqlInsert("INSERT INTO `dav_rb`.`rb_user` (`uid`, `user_name`, `user_email`, `user_password`, `route_count`, `flash_count`, `redpoint_count`, `notclimbed_count`) VALUES (NULL, 'Pierre', 'pierrechrist@gmx.de', 'test', NULL, NULL, NULL, NULL)");
		//db.syncDatabase();
		
		routeList = (Button) this.findViewById(R.id.menuRoutesButton);
		personalStats = (Button) this.findViewById(R.id.menuPersonalButton);
	        
		routeList.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent routesListActivity = new Intent(getApplicationContext(), RoutesListActivity.class);

	              startActivity(routesListActivity);
	          }
	        });
	        
	        
		personalStats.setOnClickListener(new OnClickListener()
	        {
	          public void onClick(View v)
	          {
	              Intent personalDetailsActivity = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
	              
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

}
