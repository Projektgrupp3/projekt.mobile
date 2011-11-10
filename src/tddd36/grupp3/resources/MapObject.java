package tddd36.grupp3.resources;
<<<<<<< HEAD

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
=======

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MapObject extends OverlayItem {
	private GeoPoint gp;
	private String header, message;
	private int icon;
	private String address;

	public MapObject(GeoPoint gp, String header, String message, int icon){
		super(gp, header, message);
		this.header = header;
		this.message = message;
		this.gp = gp;
		this.icon = icon;
	}
	
	public void setGeoPoint(GeoPoint gp){
		this.gp = gp;
	}

>>>>>>> master
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
<<<<<<< HEAD
	}
	public Drawable getIcon(){
		return d;
=======
	}
	public int getIcon(){
		return icon;
	}
	
	public void setAdress(String address){
		this.address = address;
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getObjectDescription(){
		return "Objekt: "+getTitle()+"\n"+
			"Meddelande: "+getMessage()+"\n"+
			"Latitud: "+getLatE6()+"\n"+
			"Longitud: "+getLonE6()+"\n"+
			"Adress: "+ getAddress()+"\n";
>>>>>>> master
	}
}
