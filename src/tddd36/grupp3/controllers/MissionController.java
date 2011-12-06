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
	static MissionModel mm;

	private UpdateMission updateMission;

	public MissionController(MissionTabView mv){
		this.mv = mv;
		mm = new MissionModel(mv, this);
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}
	public static Event getActiveMission(){
		return mm.getCurrentEvent();
	}
	public GeoPoint getActiveMissionAddress(){
		return mm.getCurrentGeoPoint();
	}
	public static void setActiveMission(Event ev){
		if(ev != null){
			mm.setActiveMission(ev);
			MainView.db.addRow(ev);
		}else{
			mm.setActiveMission(null);
		}
	}
	public static boolean hasActiveMission(){
		return mm.hasActiveMission();
	}
	public MissionModel getMissionModel() {
		return mm;
	}
	public void setMissionModel(MissionModel mm) {
		this.mm = mm;
	}
	public void addHistoryItem(String[] item){
		mm.addHistoryItem(item);
	}


}
