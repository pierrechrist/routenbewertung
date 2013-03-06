package dav.routenbewerter;

import com.dav.routenbewerter.R;
import com.db4o.ObjectSet;

import android.app.Activity;
import android.content.Context;
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
            TextView number = (TextView)currentView.findViewById(R.id.listRouteNumber);
            number.setText(Integer.toString(currentRoute.getRouteNumber()));
            TextView color = (TextView)currentView.findViewById(R.id.listRouteColor);
            color.setText(currentRoute.getHandleColor());
            TextView rating = (TextView)currentView.findViewById(R.id.listRouteRating);
            rating.setText(currentRoute.getAverageRating());
            TextView wall = (TextView)currentView.findViewById(R.id.listRouteWallName);
            wall.setText(currentRoute.getWallName());
            //ImageView start = (ImageView)currentView.findViewById(R.id.listAmpelImage);
            
            return currentView;
    }

}