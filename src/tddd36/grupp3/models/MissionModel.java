package tddd36.grupp3.models;

import java.util.ArrayList;
import java.util.Observable;

import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.Status;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionTabView;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class MissionModel extends Observable{

	private Event currentEvent;

	private static Status status;

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
				status = Status.RECIEVED;
			} else {
			}
		}
	}
	public void addHistoryItem(String[] historyItem){
		setChanged();
		notifyObservers(historyItem);
	}
	public void setCurrentMission(Event ev){
		if(ev != null){
			currentEvent = ev;
			status = Status.RECIEVED;
			setChanged();
			notifyObservers(ev);
		}else{
			currentEvent = null;
			Log.d("IncomingEvent","Event = null");
		}
	}
	public GeoPoint getCurrentGeoPoint(){
		if(this.currentEvent != null){
			return this.currentEvent.getGeoPoint();
		}
		return null;
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

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}

	public static void setStatus(Status status){
		MissionModel.status = status;
	}

	public static Status getStatus() {
		return status;
	}
}
