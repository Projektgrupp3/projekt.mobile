package tddd36.grupp3.resources;
<<<<<<< HEAD

import tddd36.grupp3.R;
=======
<<<<<<< HEAD

import android.graphics.drawable.Drawable;
=======

import tddd36.grupp3.R;
>>>>>>> master
>>>>>>> davve

import com.google.android.maps.GeoPoint;

public class Hospital extends MapObject {

<<<<<<< HEAD
=======
<<<<<<< HEAD
	private int capacity;
	private static Drawable d;

	public Hospital(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, d);
		this.capacity = capacity;		
	}	
=======
>>>>>>> davve
	private int capacity;

	public Hospital(GeoPoint gp, String header, String message, int capacity) {
		super(gp, header, message, R.drawable.hospital_icon);	
}
<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> davve
	
	public int getCapacity(){
		return capacity;
	}
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> davve

	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		return objectDesc +"Kapacitet: "+capacity;
	}
<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> davve

}
