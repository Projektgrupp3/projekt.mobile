package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import android.location.LocationManager;

import tddd36.grupp3.models.MapModel;
import tddd36.grupp3.views.MapGUI;

public class MapController implements Observer, Runnable{
	
	MapModel mapmodel;
	Thread thread = new Thread(this);
	
	public MapController(MapGUI mapgui){
		mapmodel = new MapModel(mapgui, this);
		thread.run();
	}
	
	public void update(Observable observable, Object data) {
		
	}
	
	public LocationManager getLocationManager(){
		return mapmodel.getLocationManager();
	}
	
	public MapModel getMapModel(){
		return mapmodel;
	}
	
	public void run() {
		//TODO
	}

}
