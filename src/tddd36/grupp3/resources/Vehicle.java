package tddd36.grupp3.resources;

import com.google.android.maps.GeoPoint;
import tddd36.grupp3.R;

public class Vehicle extends MapObject {
	private int capacity;
	
	public Vehicle(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, R.drawable.ambulance_icon);		
		this.capacity = capacity;		
	}	
	
	public int getCapacity(){
		return capacity;
	}
	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		return objectDesc+"Kapacitet: "+capacity;
	}

}
