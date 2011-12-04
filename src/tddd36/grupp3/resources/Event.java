package tddd36.grupp3.resources;

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
	private static GeoPoint gp;
	private int icon;

	/**
	 * Konstruktor för att lägga til ett "map-event" såsom ett nedfallet träd.
	 * @param gp - Geografiska punkten för händelsen
	 * @param header - Händelsens rubrik
	 * @param description - Händelsens fritext beskrivning
	 * @throws JSONException
	 */
	public Event(GeoPoint gp, String header, String message, String eventID, int icon ) throws JSONException{
		super(gp, header, message, icon, ObjectType.EVENT);
		this.eventID = eventID;
		this.icon = icon;
		Log.d("JSON", header);
		Log.d("JSON", message);
	}

	public Event(GeoPoint gp, int numberOfInjuried, String accidentType, String priority, 
			String typeOfInjury, String eventID, String unitID, String description){
		super(gp, accidentType, description, R.drawable.event_icon, ObjectType.EVENT);

		this.numberOfInjured = numberOfInjuried;
		this.accidentType = accidentType;
		this.priority = priority;
		this.address = MapModel.getAddress(gp);
		this.typeOfInjury = typeOfInjury;
		this.eventID = eventID;
		this.unitID = unitID;
		this.description = description;
	}

	public Event(JSONObject event, int icon) throws JSONException{
		super((gp = new GeoPoint(event.getInt("tempCoordX"),
				event.getInt("tempCoordY"))),
				event.getString("accidentType"),
				event.get("description").toString(),
				icon, ObjectType.EVENT);

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

		Log.d("JSON", "Event ID: "+eventID);
		Log.d("JSON", event.getString("accidentType"));
		Log.d("JSON", event.get("description").toString());
		Log.d("JSON", ""+numberOfInjured);
		Log.d("JSON", accidentType);
		Log.d("JSON", coordinateX);
		Log.d("JSON", coordinateY);
		Log.d("JSON", priority);
		//		Log.d("JSON", address);
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
		return objectDesc + 
		"ID: "+eventID +"\n"+
		"Prioritet: "+priority +"\n"+
		"Adress: " +address + "\n"+
		"Antal skadade: " + numberOfInjured+ "\n"+
		"Typ av skador: " + typeOfInjury + "\n"+
		"Tilldelad enhet: "+unitID;
	}
}
