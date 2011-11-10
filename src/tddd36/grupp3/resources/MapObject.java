package tddd36.grupp3.resources;


import android.content.Context;
import android.graphics.drawable.Drawable;

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
	}
}
