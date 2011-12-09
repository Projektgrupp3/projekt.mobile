package tddd36.grupp3.models;

import java.util.ArrayList;

import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.MapObject;
import android.app.Activity;
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
	
	public void remove(final MapObject item){
		((Activity)c).runOnUiThread(new Runnable(){
			public void run() {
				String oId = item.getID();
				for(int i = 0; i < pinpoints.size(); i++){
					if(((MapObject)pinpoints.get(i)).getID().equals(oId)){
						pinpoints.remove(i);
						setLastFocusedIndex(-1);
						populate();
						return;
					}
				}
			}
		});
	}
	
	@Override
	public boolean onTap(int index){
		final MapObject clicked = (MapObject) pinpoints.get(index);
		AlertDialog dialog = new AlertDialog.Builder(c).create();
		dialog.setTitle(clicked.getHeader());
		dialog.setMessage(clicked.getObjectDescription());
		dialog.setButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
		return true;
	}

	public void updateMapObject(final Event activeMission) {
		((Activity)c).runOnUiThread(new Runnable(){
			public void run() {
				String oId = activeMission.getID();
				for(OverlayItem oi:pinpoints){
					if(((MapObject)oi).getID().equals(oId)){
						pinpoints.remove(oi);
						pinpoints.add(activeMission);
						setLastFocusedIndex(-1);
						populate();
						return;
					}
				}
			}
		});
	}


}
