package tddd36.grupp3.resources;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

public class Hospital extends MapObject {

	private int capacity;
	private static Drawable d;

	public Hospital(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, d);
		this.capacity = capacity;		
	}	
	
	public int getCapacity(){
		return capacity;
	}

}
