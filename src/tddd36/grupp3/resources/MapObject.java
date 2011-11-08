package tddd36.grupp3.resources;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MapObject extends OverlayItem {
	GeoPoint gp;
	String header, message;
	Drawable d;

	public MapObject(GeoPoint gp, String header, String message) {
		super(gp, header, message);
	}
	public MapObject(GeoPoint gp, String header, String message, Drawable d){
		this(gp, header, message);
		this.d = d;
	}
	public int getLatE6(){
		return gp.getLatitudeE6();
	}
	public int getLonE6(){
		return gp.getLongitudeE6();
	}
	public String getTitle(){
		return header;
	}
	public String getMessage(){
		return message;
	}
	public Drawable getIcon(){
		return d;
	}
}
