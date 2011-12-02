package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.MissionModel;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MissionTabView;
import tddd36.grupp3.views.UpdateMission;

import com.google.android.maps.GeoPoint;

public class MissionController implements Observer {
	MissionTabView mv;
	MissionModel mm;
	MissionModel mm1;

	private UpdateMission updateMission;

	public MissionController(MissionTabView mv){
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
		if(ev != null){
			mm.setCurrentMission(ev);
			MainView.db.addRow(ev);
		}


	}


}
