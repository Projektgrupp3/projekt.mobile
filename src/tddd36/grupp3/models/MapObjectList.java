package tddd36.grupp3.models;

import java.util.ArrayList;

import tddd36.grupp3.resources.MapObject;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


public class MapObjectList extends ItemizedOverlay<OverlayItem>{

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
		return pinpoints.size();
	}
	
	public void add(MapObject item){
		pinpoints.add(item);
		this.populate();
	}
	
	@Override
	public boolean onTap(int index){
		final MapObject clicked = (MapObject) pinpoints.get(index);
		AlertDialog dialog = new AlertDialog.Builder(c).create();
		dialog.setTitle(clicked.getTitle());
		dialog.setMessage(clicked.getObjectDescription());
		dialog.setButton("�ndra Beskrivning", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		dialog.setButton2("Ta bort objekt", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//pinpoints.remove(clicked);
			}
		});
		dialog.show();
		return true;
	}


}
