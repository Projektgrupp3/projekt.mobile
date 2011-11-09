package tddd36.grupp3.models;

import java.util.Observable;

import tddd36.grupp3.controllers.MapController;
import tddd36.grupp3.views.MapGUI;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MapModel extends Observable implements LocationListener{
	int lat, lon;
	
	MapObjectList mol;
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
		OverlayItem currentLocation = new OverlayItem(ourLocation,"Rubrik","Text");
		
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
	
}
