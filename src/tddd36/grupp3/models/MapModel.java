package tddd36.grupp3.models;

import java.util.Observable;

import tddd36.grupp3.controllers.MapController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.Hospital;
import tddd36.grupp3.resources.MapObject;
import tddd36.grupp3.resources.Vehicle;
import tddd36.grupp3.views.MapGUI;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class MapModel extends Observable implements LocationListener{
	int lat, lon;
	
	private Drawable d;
	private MapObjectList vehicles,hospital,event;
	MapGUI mapgui;
	private LocationManager lm;
	
	GeoPoint ourLocation, touchedLocation;
	
	public MapModel(MapGUI mapgui, MapController mc){
		this.mapgui = mapgui;
		this.addObserver(mc);
		this.addObserver(mapgui);
		
		lm = (LocationManager) mapgui.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		Location lastKnownLocation =
			lm.getLastKnownLocation(lm.getBestProvider(criteria, true));
		lat = (int) (lastKnownLocation.getLatitude() * 1E6);
		lon = (int) (lastKnownLocation.getLongitude() * 1E6);
		GeoPoint lastKnownGeoPoint = new GeoPoint(lat,lon);
		
		setChanged();
		notifyObservers(lastKnownGeoPoint);		
	}
	
	public void onLocationChanged(Location location) {
		lat = (int) (location.getLatitude() * 1E6);
		lon = (int) (location.getLongitude() * 1E6);
		GeoPoint ourLocation = new GeoPoint(lat, lon);
		Vehicle currentLocation = new Vehicle(ourLocation,"Min plats","Här är jag",2);
		setChanged();
		notifyObservers(currentLocation);		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public LocationManager getLocationManager(){
		return lm;
	}
	
	public void addMapObject(MapObject o){
		d = mapgui.getResources().getDrawable(o.getIcon());
		setChanged();
		if(o instanceof Vehicle){
			if(vehicles == null){
				vehicles = new MapObjectList(d, mapgui);
			}
			vehicles.add(o);
			notifyObservers(vehicles);
		}
		else if(o instanceof Hospital){
			if(hospital == null){
				Toast.makeText(mapgui.getBaseContext(), "SjukhusBajs", Toast.LENGTH_SHORT).show();
				hospital = new MapObjectList(d, mapgui);
			}
			hospital.add(o);
			notifyObservers(hospital);
		}
		else if(o instanceof Event){
			if(event == null){
				event = new MapObjectList(d, mapgui);
			}
			event.add(o);
			notifyObservers(event);
		}
		
	}
	
}
