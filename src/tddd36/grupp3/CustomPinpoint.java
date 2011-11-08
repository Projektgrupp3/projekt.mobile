package tddd36.grupp3;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class CustomPinpoint extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
	private Context c;
	
	public CustomPinpoint(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView){
		Toast.makeText(mapView.getContext(), "test", Toast.LENGTH_SHORT);
		return false;
	}
	
	public CustomPinpoint(Drawable m, Context context) {
		this(m);
		c = context;
	}
	@Override
	protected OverlayItem createItem(int i) {
		return pinpoints.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return pinpoints.size();
	}
	
	public void insertPinpoint(OverlayItem item){
		pinpoints.add(item);
		this.populate();
	}
	

}
