package tddd36.grupp3.models;

import java.util.ArrayList;
import java.util.Observable;

import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MissionView;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class MissionModel extends Observable{

	private GeoPoint gp;
	private String eventheader;
	private String eventdescription;
	private String eventaddress;
	private String time;
	private String nmbrofinjuried;
	ArrayList<Event> currentMissionFromDB;

	private MissionView mv;
	private MissionController mc;

	public MissionModel(MissionView mv, MissionController mc) {
		this.mv = mv;
		this.mc = mc;

		//getCurrentMissionFromDB();

		addObserver(mv);	
		addObserver(mc);
	}

	@SuppressWarnings("unchecked")
	private void getCurrentMissionFromDB() {
		currentMissionFromDB = new ArrayList<Event>();
		currentMissionFromDB = MainView.db.getAllRowsAsArrayList("mission");
		if(currentMissionFromDB.isEmpty()){
			Log.e("bajs", "Tom lista");
		}
		if(currentMissionFromDB.get(0) != null){
			setCurrentMission(currentMissionFromDB.get(0));
		}		
	}

	public void setCurrentMission(Event ev){
		if(ev != null){
			gp = ev.getPoint();
			eventheader = ev.getTitle();
			eventdescription = ev.getMessage();
			eventaddress = ev.getAddress();
			time = ev.getTime();
			nmbrofinjuried = ev.getInjuried();

			String[] currentmission = {eventheader,eventdescription,eventaddress,time, nmbrofinjuried};

			setChanged();
			notifyObservers(currentmission);
		}
	}
	public GeoPoint getCurrentGeoPoint(){
		return gp;
	}

	public String getEventheader() {
		return eventheader;
	}

	public void setEventheader(String eventheader) {
		this.eventheader = eventheader;
	}

	public String getEventdescription() {
		return eventdescription;
	}

	public void setEventdescription(String eventdescription) {
		this.eventdescription = eventdescription;
	}

	public String getAddress() {
		return eventaddress;
	}

	public void setAddress(String address) {
		this.eventaddress = address;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNmbrofinjuried() {
		return nmbrofinjuried;
	}

	public void setNmbrofinjuried(String nmbrofinjuried) {
		this.nmbrofinjuried = nmbrofinjuried;
	}
}
