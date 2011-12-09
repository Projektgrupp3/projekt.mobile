package tddd36.grupp3.models;

import java.util.ArrayList;
import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.misc.Journal;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.Status;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionTabView;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class MissionModel extends Observable{

	private static Event currentEvent;
	private static Journal currentJournal;
	private static Status status;

	ArrayList<Event> currentMissionFromDB;

	private MissionTabView mv;

	public MissionModel(MissionTabView mv) {
		this.mv = mv;
		this.addObserver(mv);
		
		getCurrentMissionFromDB();
		Log.d("MissionModel","Nu har den hämtat uppdraget från databasen");
	}

	@SuppressWarnings("unchecked")
	private void getCurrentMissionFromDB() {
		ArrayList<Event> events = MainView.db.getAllRowsAsArrayList("mission");
		if(events.size() > 0){
			if(events.get(0) != null && events.get(0).isActive()){
				currentEvent = events.get(0);
				setChanged();
				notifyObservers(currentEvent);
			}
		}
	}
	
	public void setActiveMission(Event ev){
		if(ev != null){
			currentEvent = ev;
			status = Status.RECIEVED;
			setChanged();
			notifyObservers(currentEvent);
		}else{
			currentEvent = null;
			setChanged();
			notifyObservers(null);
		}
	}

	public boolean hasActiveMission(){
		if(currentEvent != null){
			return true;
		}else 
			return false;
	}
	public GeoPoint getCurrentGeoPoint(){
		if(MissionModel.currentEvent != null){
			return MissionModel.currentEvent.getGeoPoint();
		}
		return null;
	}

	public String getEventID() {
		return MissionModel.currentEvent.getID();
	}

	public void setEventID(String eventID) {
		MissionModel.currentEvent.setID(eventID);
	}

	public String getAccidentType() {
		return currentEvent.getAccidentType();
	}

	public void setAccidentType(String accidentType) {
		MissionModel.currentEvent.setAccidentType(accidentType);
	}

	public int getNumberOfInjured() {
		return MissionModel.currentEvent.getNumberOfInjured();
	}

	public void setNumberOfInjured(int numberOfInjured) {
		MissionModel.currentEvent.setNumberOfInjured(numberOfInjured);
	}

	public String getPriority() {
		return MissionModel.currentEvent.getPriority();
	}

	public void setPriority(String priority) {
		MissionModel.currentEvent.setPriority(priority);
	}

	public String getAdress() {
		return MissionModel.currentEvent.getAddress();
	}

	public void setAdress(String adress) {
		MissionModel.currentEvent.setAdress(adress);
	}

	public String getTypeOfInjury() {
		return MissionModel.currentEvent.getTypeOfInjury();
	}

	public void setTypeOfInjury(String typeOfInjury) {
		MissionModel.currentEvent.setTypeOfInjury(typeOfInjury);
	}

	public String getDescription() {
		return MissionModel.currentEvent.getDescription();
	}

	public void setDescription(String description) {
		MissionModel.currentEvent.setDescription(description);
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public static void setCurrentEvent(Event currentEvent) {
		MissionModel.currentEvent = currentEvent;
	}

	public static void setStatus(Status status){
		MissionModel.status = status;
	}

	public static Status getStatus() {
		return status;
	}

	public void setMissionTabView(MissionTabView missionTabView) {
		this.mv = missionTabView;
		addObserver(mv);
	}

	public void setActiveJournal(JSONObject obj) {
		try {
			currentJournal = new Journal(obj.getString("Name"),obj.getString("SSN"),obj.getString("Address"),
					obj.getString("Bloodtype"),obj.getString("Allergies"),obj.getString("Warning"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers(currentJournal);
	}
}
