package tddd36.grupp3.models;

import java.util.Observable;

import tddd36.grupp3.views.MissionView;

public class MissionModel extends Observable{
	private String eventheader;
	private String eventdescription;
	private String address;
	private String time;
	private int nmbrofinjuried;
	
	private MissionView view;
	
	public MissionModel(MissionView view, String hdr, String desc, String adr, String time, int inj){
		this.view = view;
		eventheader = hdr;
		eventdescription = desc;
		address = adr;
		this.time = time;
		nmbrofinjuried = inj;
		addObserver(view);		
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
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getNmbrofinjuried() {
		return nmbrofinjuried;
	}

	public void setNmbrofinjuried(int nmbrofinjuried) {
		this.nmbrofinjuried = nmbrofinjuried;
	}
}
