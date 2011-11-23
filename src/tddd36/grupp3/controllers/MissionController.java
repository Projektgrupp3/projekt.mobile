package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.MissionModel;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MissionView;

import com.google.android.maps.GeoPoint;

public class MissionController implements Observer {
	MissionView mv;
	MissionModel mm;
	
	public MissionController(MissionView mv){
		this.mv = mv;
		mm = new MissionModel(mv, this);
	}
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
	public GeoPoint getCurrentMissionAddress(){
		return mm.getCurrentGeoPoint();
	}
	public void setCurrentMission(Event ev){
		mm.setCurrentMission(ev);
		MainView.db.addRow(ev);
	}

}
