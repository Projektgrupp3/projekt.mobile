package tddd36.grupp3.resources;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import tddd36.grupp3.R;
import tddd36.grupp3.views.MapGUI;

public class Event extends MapObject{
	private String injuried;
	private String time;


	public String getInjuried(){
		return injuried;
	}

	public String getTime(){
		return time;
	}

	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		return objectDesc+"Tid: "+time;
	}

	// KODEN OVAN �R EMILS DUMMYKOD
	//
	//
	//
	//

	private String eventID;
	private String accidentType;
	private String coordinateX;
	private String coordinateY;
	private int numberOfInjured;
	private String priority; 
	private String adress;
	private String typeOfInjury;
	private int unitID;
	private String description;
	private static GeoPoint gp;

	public Event(JSONObject event) throws JSONException{
		super((gp = new GeoPoint(event.getInt("tempCoordX"),
				event.getInt("tempCoordY"))),
				event.getString("accidentType"),
				event.get("description").toString(),
				R.drawable.event_icon, ObjectType.EVENT);

		this.numberOfInjured = event.getInt("numberOfInjured");
		this.accidentType = event.getString("accidentType");
		this.coordinateX = event.getString("tempCoordX"); // lat
		this.coordinateY = event.getString("tempCoordY"); // long
		this.priority = event.getString("priority");
		this.adress = this.getAddress();
		this.typeOfInjury = event.getString("typeOfInjury");
		this.eventID = event.getString("event");
		this.unitID = event.getInt("unitID");
		this.description = event.getString("description");

		Log.d("JSON", "Event ID: "+eventID);
		Log.d("JSON", event.getString("accidentType"));
		Log.d("JSON", event.get("description").toString());
		Log.d("JSON", ""+numberOfInjured);
		Log.d("JSON", accidentType);
		Log.d("JSON", coordinateX);
		Log.d("JSON", coordinateY);
		Log.d("JSON", priority);
//		Log.d("JSON", adress);
		Log.d("JSON", typeOfInjury);
		Log.d("JSON", ""+unitID);

	}
	
	public Event(GeoPoint gp, int numberOfInjuried, String accidentType, String priority, 
			String typeOfInjury, String eventID, int unitID, String description){
		super(gp, accidentType, description, R.drawable.event_icon, ObjectType.EVENT);
		
		this.numberOfInjured = numberOfInjuried;
		this.accidentType = accidentType;
		this.priority = priority;
		this.adress = this.getAddress();
		this.typeOfInjury = typeOfInjury;
		this.eventID = eventID;
		this.unitID = unitID;
		this.description = description;
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

	public void setAdress(String adress) {
		this.adress = adress;
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

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
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
}
