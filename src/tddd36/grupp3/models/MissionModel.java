package tddd36.grupp3.models;

import java.util.Observable;

import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MissionView;

import com.google.android.maps.GeoPoint;

public class MissionModel extends Observable{
	
	private GeoPoint gp;
	private String eventheader;
	private String eventdescription;
	private String eventaddress;
	private String time;
	private String nmbrofinjuried;
	
	
	private MissionView mv;
	private MissionController mc;
	
	public MissionModel(MissionView mv, MissionController mc) {
		this.mv = mv;
		this.mc = mc;

		addObserver(mv);	
		addObserver(mc);
	}

	public void setCurrentMission(Event ev){
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
