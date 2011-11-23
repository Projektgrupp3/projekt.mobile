package tddd36.grupp3.models;

import java.util.ArrayList;
import java.util.Observable;

import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionView;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class MissionModel extends Observable{

	private GeoPoint gp;
	private String eventID;
	private String priority; 
	private String accidentType;
	private String adress;
	private String numberOfInjured;
	private String typeOfInjury;
	private String description;

	ArrayList<Event> currentMissionFromDB;

	private MissionView mv;
	private MissionController mc;

	public MissionModel(MissionView mv, MissionController mc) {
		this.mv = mv;
		this.mc = mc;

		addObserver(mv);	
		addObserver(mc);
		
//		getCurrentMissionFromDB();
	}

	@SuppressWarnings("unchecked")
	private void getCurrentMissionFromDB() {
		try{
		setCurrentMission(MainView.db.getCurrentEvent());	
		} catch(NullPointerException e){
			
		}
	}

	public void setCurrentMission(Event ev){
		if(ev != null){
			gp = ev.getPoint();

			eventID = ev.getID();
			priority = ev.getPriority();
			accidentType = ev.getAccidentType();
			adress = MapGUI.mapcontroller.getMapModel().getAddress(gp);
			numberOfInjured = ""+ev.getNumberOfInjured();
			typeOfInjury = ev.getTypeOfInjury();
			description = ev.getDescription();

			String[] currentmission = {eventID, priority,accidentType,adress,numberOfInjured,typeOfInjury,description};

			setChanged();
			notifyObservers(currentmission);
		}
	}
	public GeoPoint getCurrentGeoPoint(){
		return gp;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getNumberOfInjured() {
		return numberOfInjured;
	}

	public void setNumberOfInjured(String numberOfInjured) {
		this.numberOfInjured = numberOfInjured;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getTypeOfInjury() {
		return typeOfInjury;
	}

	public void setTypeOfInjury(String typeOfInjury) {
		this.typeOfInjury = typeOfInjury;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MissionView getMv() {
		return mv;
	}

	public void setMv(MissionView mv) {
		this.mv = mv;
	}

	public MissionController getMc() {
		return mc;
	}

	public void setMc(MissionController mc) {
		this.mc = mc;
	}
}
