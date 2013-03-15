package dav.routenbewerter;

import com.dav.routenbewerter.R;
import com.db4o.ObjectSet;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RouteAdapter extends ArrayAdapter<Route> {
	private ObjectSet<Route> route;
	private int textViewResourceId;
	private Context context;
	
	public RouteAdapter(Context context, int textViewResourceId, ObjectSet<Route> route) {
		super(context, textViewResourceId);
		this.route = route;
		this.textViewResourceId = textViewResourceId;
        this.context = context;
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
            
            String icon = currentRoute.getHandleColor().replaceAll(" ", "_").replaceAll("�", "ue");
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
            //TextView color = (TextView)currentView.findViewById(R.id.listRouteColor);
            //color.setText(currentRoute.getHandleColor());
            TextView rating = (TextView)currentView.findViewById(R.id.listRouteRating);
            rating.setText(currentRoute.getAverageRating());
            TextView wall = (TextView)currentView.findViewById(R.id.listRouteWallName);
            wall.setText(currentRoute.getWallName());
            //ImageView start = (ImageView)currentView.findViewById(R.id.listAmpelImage);
            
            return currentView;
    }

}