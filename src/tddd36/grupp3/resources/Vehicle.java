package tddd36.grupp3.resources;

<<<<<<< HEAD
import com.google.android.maps.GeoPoint;
import tddd36.grupp3.R;

public class Vehicle extends MapObject {
	private int capacity;
	
	public Vehicle(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, R.drawable.ambulance_icon);		
=======
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

public class Vehicle extends MapObject {
	private int capacity;
	static Drawable d;	
	
	public Vehicle(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, d);		
>>>>>>> davve
		this.capacity = capacity;		
	}	
	
	public int getCapacity(){
		return capacity;
	}
<<<<<<< HEAD
	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		return objectDesc+"Kapacitet: "+capacity;
	}
=======
>>>>>>> davve

}
