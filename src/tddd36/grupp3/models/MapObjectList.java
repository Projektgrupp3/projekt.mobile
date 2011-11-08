package tddd36.grupp3.models;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class MapObjectList extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
	private Context c;
	
	public MapObjectList(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}
	
	public MapObjectList(Drawable m, Context context) {
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
	
	@Override
	public boolean onTap(int index){
		OverlayItem clicked = pinpoints.get(index);
		AlertDialog dialog = new AlertDialog.Builder(c).create();
		dialog.setTitle(clicked.getTitle());
		dialog.setMessage(clicked.getSnippet());
		dialog.show();
		return true;
	}

}
