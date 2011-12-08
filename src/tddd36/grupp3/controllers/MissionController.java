package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.MissionModel;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MissionTabView;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class MissionController implements Observer {
	MainView mainView;
	MissionTabView mv;
	static MissionModel mm;

	public MissionController(MainView mainView){
		this.mainView = mainView;
	}
	public void setMissionView(MissionTabView missionTabView){
		this.mv = missionTabView;
		mm = new MissionModel(missionTabView);
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}
	public Event getActiveMission(){
		return mm.getCurrentEvent();
	}
	public GeoPoint getActiveMissionAddress(){
		return mm.getCurrentGeoPoint();
	}
	public void setActiveMission(Event ev){
		if(mm != null){
			if(ev.isActive()){
				mm.setActiveMission(ev);
				MainView.db.addRow(ev);
			}else{
				MainView.db.updateRow(ev);
				mm.setActiveMission(null);
			}
		}
	}

	public void updateActiveMission(Event ev){
		if(ev != null){
			mm.setActiveMission(ev);
			MainView.db.updateRow(ev);
			Log.d("MissionController", ev.getDescription());
		}
	}
	public boolean hasActiveMission(){
		return mm.hasActiveMission();
	}
	public MissionModel getMissionModel() {
		return mm;
	}
	public void setMissionModel(MissionModel mm) {
		this.mm = mm;
	}

	public void setMissionTabView(MissionTabView missionTabView) {
		this.mv = missionTabView;
		this.mm.setMissionTabView(missionTabView);
	}
}
