package tddd36.grupp3.resources;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.R;
import tddd36.grupp3.models.MapModel;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class Event extends MapObject{

	private String eventID;
	private String accidentType;
	private String coordinateX;
	private String coordinateY;
	private int numberOfInjured;
	private String priority; 
	private String address;
	private String typeOfInjury;
	private String unitID;
	private String description;
	private String lastChanged;
	private static GeoPoint gp;
	private int icon;
	private boolean accepted, isActive;

	/**
	 * Konstruktor för att l�gga til ett "map-event" s�som ett nedfallet tr�d.
	 * @param gp - Geografiska punkten f�r h�ndelsen
	 * @param header - H�ndelsens rubrik
	 * @param description - H�ndelsens fritext beskrivning
	 * @throws JSONException
	 */
	
	public Event(GeoPoint gp, String header, String message, String eventID, int icon ) throws JSONException{
		super(gp, header, message, icon, ObjectType.EVENT, eventID);
		this.eventID = eventID;
		this.icon = icon;
		this.address = MapModel.getAddress(gp);
		Log.d("JSON", header);
		Log.d("JSON", message);
	}

	public Event(GeoPoint gp, int numberOfInjuried, String accidentType, String priority, 
			String typeOfInjury, String eventID, String unitID, String description){
		super(gp, accidentType, description, R.drawable.event_icon, ObjectType.EVENT, eventID);

		this.numberOfInjured = numberOfInjuried;
		this.accidentType = accidentType;
		this.priority = priority;
		this.address = MapModel.getAddress(gp);
		this.typeOfInjury = typeOfInjury;
		this.eventID = eventID;
		this.unitID = unitID;
		this.description = description;
		lastChanged = new SimpleDateFormat("yy:MM:dd:HH:mm:ss").format(new Date());
	}

	public Event(JSONObject event, int icon) throws JSONException{
		super((gp = new GeoPoint(event.getInt("tempCoordX"),
				event.getInt("tempCoordY"))),
				event.getString("accidentType"),
				event.get("description").toString(),
				icon, ObjectType.EVENT,
				event.getString("event"));

		this.numberOfInjured = event.getInt("numberOfInjured");
		this.accidentType = event.getString("accidentType");
		this.coordinateX = event.getString("tempCoordX"); // lat
		this.coordinateY = event.getString("tempCoordY"); // long
		this.priority = event.getString("priority");
		this.address = MapModel.getAddress(gp);
		this.typeOfInjury = event.getString("typeOfInjury");
		this.eventID = event.getString("event");
		this.unitID = event.getString("unitID");
		this.description = event.getString("description");
		lastChanged = new SimpleDateFormat("yy:MM:dd:HH:mm:ss").format(new Date());
		isActive = true;
		
		Log.d("JSON", "Event ID: "+eventID);
		Log.d("JSON", event.getString("accidentType"));
		Log.d("JSON", event.get("description").toString());
		Log.d("JSON", ""+numberOfInjured);
		Log.d("JSON", accidentType);
		Log.d("JSON", coordinateX);
		Log.d("JSON", coordinateY);
		Log.d("JSON", priority);
		Log.d("JSON", typeOfInjury);
		Log.d("JSON", ""+unitID);

	}

	public int getNumberOfInjured() {
		return numberOfInjured;
	}
	public void setNumberOfInjured(int numberOfInjured) {
		this.numberOfInjured = numberOfInjured;
	}

	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType){
		this.accidentType= accidentType;
	}
	public String getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(String coordinateX){
		this.coordinateX= coordinateX;
	}

	public String getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(String coordinateY){
		this.coordinateY= coordinateY;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setAdress(String address) {
		this.address = address;
	}
	public String getAddress(){
		return this.address;
	}

	public String getTypeOfInjury() {
		return typeOfInjury;
	}

	public void setTypeOfInjury(String typeOfInjury){
		this.typeOfInjury= typeOfInjury;
	}

	public String getID() {
		return eventID;
	}

	public void setID(String id){
		this.eventID= id;
	}

	public String getUnitID() {
		return unitID;
	}

	public void setUnitID(String unitID) {
		this.unitID = unitID;
	}

	public void setDescription(String str){
		this.description = str;
	}
	public String getDescription(){
		return description;
	}

	public String processInput(String typeOfAccident) {
		return typeOfAccident;
	}
	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		if(super.getIcon() == R.drawable.red_flag_icon){
		return  
		objectDesc +"\n"+
		"ID: "+eventID +"\n"+
		"Prioritet: "+priority +"\n"+
		"Adress: " +address + "\n"+
		"Antal skadade: " + numberOfInjured+ "\n"+
		"Typ av skador: " + typeOfInjury + "\n"+
		"Beskrivning: " + description +"\n"+
		"Tilldelad enhet: "+unitID+ "\n";
		}
		else{
			return objectDesc + 
			"ID: "+eventID +"\n"+
			"Adress: " +address;
		}
	}

	public String getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(String lastChanged) {
		this.lastChanged = lastChanged;
	}

	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean state){
		isActive = state;
	}
}
