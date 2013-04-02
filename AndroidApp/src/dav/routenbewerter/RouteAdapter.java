package dav.routenbewerter;

import java.util.List;

import com.dav.routenbewerter.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteAdapter extends ArrayAdapter<Route> {
	private List<Route> route;
	//private DBConnector db;
	private int textViewResourceId;
	private Context context;
	//private int userId;
	private List<Rating> rating;
	
	public RouteAdapter(Context context, int textViewResourceId, List<Route> route, List<Rating> rating) {
		super(context, textViewResourceId);
		this.route = route;
		//this.db = db;
		this.textViewResourceId = textViewResourceId;
        this.context = context;
        //this.userId = userId;
        this.rating = rating;
	}
	
    @Override
    public int getCount() {
        return route.size();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View currentView = convertView;
            
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            currentView = inflater.inflate(textViewResourceId, parent, false);
         
            Route currentRoute = route.get(position);
            
            String icon = currentRoute.getHandleColor().replaceAll(" ", "_").replaceAll("ü", "ue");
            int resID = parent.getResources().getIdentifier(icon, "drawable",  ((Activity)context).getPackageName()); 
            
            Drawable myIcon = null;
            if(resID != 0) {
            	myIcon = getContext().getResources().getDrawable(resID);
            } else {
            	myIcon = getContext().getResources().getDrawable(R.drawable.no_color);
    		}
            
            myIcon.setBounds(0, 0, 32, 32);
            
            TextView number = (TextView)currentView.findViewById(R.id.listRouteNumber);
            number.setText(Integer.toString(currentRoute.getBoltRow()));
            number.setCompoundDrawables(myIcon, null, null, null);
            TextView rating = (TextView)currentView.findViewById(R.id.listRouteRating);
            rating.setText(currentRoute.getRating());
            TextView wall = (TextView)currentView.findViewById(R.id.listRouteWallName);
            wall.setText(currentRoute.getWallName());
            ImageView start = (ImageView)currentView.findViewById(R.id.listAmpelImage);

            //Rating r = db.getRating(new Rating(currentRoute, new User(userId)));
            
            for(int i = 0; i < this.rating.size(); i++){
            	if(this.rating.get(i).getRoute().getRouteNumber() == currentRoute.getRouteNumber()) {
		            if(this.rating.get(i).getHowClimbed().equals("Flash"))
		            	start.setImageResource(R.drawable.traffic_lights_green);
		            else if(this.rating.get(i).getHowClimbed().equals("Rotpunkt") || this.rating.get(i).getHowClimbed().equals("Projekt"))
		            	start.setImageResource(R.drawable.traffic_lights_yellow);
		            else
		            	start.setImageResource(R.drawable.traffic_lights_red);
            	}
            }
            return currentView;
    }

}