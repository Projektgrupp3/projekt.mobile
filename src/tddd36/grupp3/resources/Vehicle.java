package tddd36.grupp3.resources;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

public class Vehicle extends MapObject {
	private int capacity;
	static Drawable d;	
	
	public Vehicle(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, d);		
		this.capacity = capacity;		
	}	
	
	public int getCapacity(){
		return capacity;
	}

}
