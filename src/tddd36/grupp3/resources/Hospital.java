package tddd36.grupp3.resources;


import tddd36.grupp3.R;

import com.google.android.maps.GeoPoint;

public class Hospital extends MapObject {

	private int capacity;

	public Hospital(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, R.drawable.hospital_icon);	
}
	
	public int getCapacity(){
		return capacity;
	}


	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		return objectDesc +"Kapacitet: "+capacity;
	}

}
