package tddd36.grupp3.resources;

<<<<<<< HEAD
import tddd36.grupp3.R;
=======
import android.graphics.drawable.Drawable;
>>>>>>> davve

import com.google.android.maps.GeoPoint;

public class Hospital extends MapObject {

	private int capacity;
<<<<<<< HEAD

	public Hospital(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, R.drawable.hospital_icon);	
=======
	private static Drawable d;

	public Hospital(GeoPoint gp, String header, String message, int capacity) {
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
		return objectDesc +"Kapacitet: "+capacity;
	}
=======
>>>>>>> davve

}
