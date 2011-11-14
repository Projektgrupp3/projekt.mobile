package tddd36.grupp3.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import com.google.android.maps.GeoPoint;

import tddd36.grupp3.models.MissionModel;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MissionView;

public class MissionController implements Observer {
	MissionView mv;
	MissionModel mm;
	String text = "Större trafikolycka i Valla-rondellen. \n"+
				"Flera skadade, möjliga nackskador.\n"+
				"Kan finnas barn inblandade i olyckan.";
	
	public MissionController(MissionView mv){
		this.mv = mv;
		mm = new MissionModel(mv, this);
		Event ev = new Event(new GeoPoint(5823837, 1534663), "Trafikolycka", text, 
				new SimpleDateFormat("HH:mm:ss").format(new Date()),"5");
		mm.setCurrentMission(ev);
		
	}
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

}
