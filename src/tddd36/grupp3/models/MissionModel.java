package tddd36.grupp3.models;

import java.util.ArrayList;
import java.util.Observable;

import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionTabView;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class MissionModel extends Observable{

	private Event currentEvent;

	ArrayList<Event> currentMissionFromDB;

	private MissionTabView mv;
	private MissionController mc;

	public MissionModel(MissionTabView mv, MissionController mc) {
		this.mv = mv;
		this.mc = mc;

		addObserver(mv);	
		addObserver(mc);
		
		getCurrentMissionFromDB();
	}

	@SuppressWarnings("unchecked")
	private void getCurrentMissionFromDB() {
		ArrayList<Event> events = MainView.db.getAllRowsAsArrayList("mission");
		if(events.size() > 1){
		if(events.get(0) != null){
			setCurrentMission(events.get(0));
		}else{
			Log.d("hej","hå");
		}
		}
	}

	public void setCurrentMission(Event ev){
		if(ev != null){
			currentEvent = ev;
			setChanged();
			notifyObservers(ev);
		}else{
			Log.d("hejhej","hoho");
		}
	}
	public GeoPoint getCurrentGeoPoint(){
		return this.currentEvent.getGeoPoint();
	}

	public String getEventID() {
		return this.currentEvent.getID();
	}

	public void setEventID(String eventID) {
		this.currentEvent.setID(eventID);
	}

	public String getAccidentType() {
		return currentEvent.getAccidentType();
	}

	public void setAccidentType(String accidentType) {
		this.currentEvent.setAccidentType(accidentType);
	}

	public int getNumberOfInjured() {
		return this.currentEvent.getNumberOfInjured();
	}

	public void setNumberOfInjured(int numberOfInjured) {
		this.currentEvent.setNumberOfInjured(numberOfInjured);
	}

	public String getPriority() {
		return currentEvent.getPriority();
	}

	public void setPriority(String priority) {
		this.currentEvent.setPriority(priority);
	}

	public String getAdress() {
		return this.currentEvent.getAddress();
	}

	public void setAdress(String adress) {
		this.currentEvent.setAdress(adress);
	}

	public String getTypeOfInjury() {
		return this.currentEvent.getTypeOfInjury();
	}

	public void setTypeOfInjury(String typeOfInjury) {
		this.currentEvent.setTypeOfInjury(typeOfInjury);
	}

	public String getDescription() {
		return this.currentEvent.getDescription();
	}

	public void setDescription(String description) {
		this.currentEvent.setDescription(description);
	}
}
