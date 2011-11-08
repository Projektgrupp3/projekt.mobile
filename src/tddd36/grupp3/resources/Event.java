package tddd36.grupp3.resources;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

public class Event extends MapObject{

	private static Drawable d;
	
	public Event(GeoPoint gp, String header, String message) {
		super(gp, header, message, d);
	}

}
