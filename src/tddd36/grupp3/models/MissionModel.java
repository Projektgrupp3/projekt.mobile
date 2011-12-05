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

	private static Event currentEvent;

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
				setActiveMission(events.get(0));
				status = Status.RECIEVED;
			}else{
				Log.d("hej","hå");
			}
		}
	}

	public void setActiveMission(Event ev){
		if(ev != null){
			currentEvent = ev;
			status = Status.RECIEVED;
			setChanged();
			notifyObservers(ev);
		}else{
			currentEvent = null;
			setChanged();
			notifyObservers(null);
		}
	}
	
	public boolean hasActiveMission(){
		if(currentEvent != null){
			return true;
		}else return false;
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
}
