package tddd36.grupp3.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.MapModel;
import tddd36.grupp3.resources.MapObject;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import com.google.android.maps.GeoPoint;

public class MapController implements Observer, Runnable{
	MapGUI mapgui;
	MapModel mapmodel;
	Thread thread = new Thread(this);
	
	public MapController(MapGUI mapgui){
		this.mapgui = mapgui;
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
	
	public void addMapObject(MapObject o){
		mapmodel.addMapObject(o, getAddress(o.getPoint()));
		MainView.db.addRow(o);
		;
	}
	
	public String getAddress(GeoPoint gp){
		String addressString = "";
		Geocoder gc = new Geocoder(mapgui.getBaseContext(), Locale.getDefault());
		try{
			List<Address> address = gc.getFromLocation(gp.getLatitudeE6()/1E6,gp.getLongitudeE6()/1E6, 1);
			if(address.size() > 0){
				for(int i = 0;i<address.get(0).getMaxAddressLineIndex();i++){
					addressString += address.get(0).getAddressLine(i) + "\n";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			//no-op
		}		
		return addressString;
	}

	public void run() {
		//TODO
	}

	public GeoPoint fireCurrentLocation() {
		return mapmodel.fireCurrentLocation();

	}

}
