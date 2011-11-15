package tddd36.grupp3.resources;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;


public class MapObject extends OverlayItem {
	private GeoPoint gp;
	private String header, message;
	private int icon;
	private String address;
	private ObjectType type;

	public MapObject(GeoPoint gp, String header, String message, int icon, ObjectType type){
		super(gp, header, message);
		this.header = header;
		this.message = message;
		this.gp = gp;
		this.icon = icon;
		this.type = type;
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
	public String getHeader(){
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

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}
	
	public String getObjectDescription(){
		return "Objekt: "+getTitle()+"\n"+
			"Meddelande: "+getMessage()+"\n"+
			"Latitud: "+getLatE6()+"\n"+
			"Longitud: "+getLonE6()+"\n"+
			"Adress: "+ getAddress()+"\n";
	}
}
