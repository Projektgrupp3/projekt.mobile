package tddd36.grupp3.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import tddd36.grupp3.controllers.MapController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.FloodEvent;
import tddd36.grupp3.resources.Hospital;
import tddd36.grupp3.resources.MapEvent;
import tddd36.grupp3.resources.MapObject;
import tddd36.grupp3.resources.OtherEvent;
import tddd36.grupp3.resources.RoadBlockEvent;
import tddd36.grupp3.resources.Vehicle;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;


public class MapModel extends Observable implements LocationListener{
	int lat, lon;

	private Drawable d;
	private MapObjectList vehicles,hospital,event, roadblock, flood, otherevent, mapevent;
	public static final String GPS_FAILED = "Kunde inte hämta GPS-status";

	static MapGUI mapgui;
	private LocationManager lm;
	private Location lastKnownLocation;
	private Criteria criteria;
	private MapObject[] mapObjectArray;
	ArrayList<MapObject> mapObjectsFromDB;


	GeoPoint ourLocation, touchedLocation, lastKnownGeoPoint;

	public MapModel(MapGUI mapgui, MapController mc){

		this.mapgui = mapgui;
		this.addObserver(mc);
		this.addObserver(mapgui);

		insertMapObjectsFromDB();

		lm = (LocationManager) mapgui.getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		lastKnownLocation =
			lm.getLastKnownLocation(lm.getBestProvider(criteria, true));
		if(lastKnownLocation != null){
			lat = (int) (lastKnownLocation.getLatitude() * 1E6);
			lon = (int) (lastKnownLocation.getLongitude() * 1E6);
			lastKnownGeoPoint = new GeoPoint(lat,lon);		
			setChanged();
			notifyObservers(lastKnownGeoPoint);		
		}else{
			Toast.makeText(mapgui.getBaseContext(), GPS_FAILED, Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressWarnings("unchecked")
	public void insertMapObjectsFromDB(){
		mapObjectsFromDB = new ArrayList<MapObject>();
		mapObjectsFromDB = MainView.db.getAllRowsAsArrayList("map");
		for(MapObject o: mapObjectsFromDB){
			if(o != null){
				addMapObject(o);
			}else return;
		}
	}

	public GeoPoint fireCurrentLocation(){
		lastKnownLocation =
			lm.getLastKnownLocation(lm.getBestProvider(criteria, true));
		if(lastKnownLocation != null){
			lat = (int) (lastKnownLocation.getLatitude() * 1E6);
			lon = (int) (lastKnownLocation.getLongitude() * 1E6);
			lastKnownGeoPoint = new GeoPoint(lat,lon);		
			setChanged();
			return lastKnownGeoPoint;	
		}else{
			Toast.makeText(mapgui.getBaseContext(), GPS_FAILED, Toast.LENGTH_SHORT).show();
			return null;
		}		
	}

	public void onLocationChanged(Location location) {
		lat = (int) (location.getLatitude() * 1E6);
		lon = (int) (location.getLongitude() * 1E6);
		lastKnownGeoPoint= new GeoPoint(lat, lon);

		setChanged();
		notifyObservers(lastKnownGeoPoint);		

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
		if(o != null){
			d = mapgui.getResources().getDrawable(o.getIcon());
			o.setAdress(getAddress(o.getPoint()));
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
					hospital = new MapObjectList(d, mapgui);
				}
				hospital.add(o);
				notifyObservers(hospital);
			}
			else if(o instanceof Event){
				if(o instanceof RoadBlockEvent){
					if(roadblock == null){
						roadblock = new MapObjectList(d, mapgui);
					}
					roadblock.add(o);
					notifyObservers(roadblock);
				}
				else if(o instanceof FloodEvent){
					if(flood == null){
						flood  = new MapObjectList(d, mapgui);
					}
					flood .add(o);
					notifyObservers(flood );
				}
				else if(o instanceof OtherEvent){
					if(otherevent == null){
						otherevent = new MapObjectList(d, mapgui);
					}
					otherevent.add(o);
					notifyObservers(otherevent);
				}
				else if(o instanceof MapEvent){
					if(mapevent == null){
						mapevent = new MapObjectList(d, mapgui);
					}
					mapevent.add(o);
					notifyObservers(mapevent);
				}
				
				else if(o instanceof Event){
					if(event == null){
						event = new MapObjectList(d, mapgui);
						event.add(o);
						notifyObservers(event);
					}
				}

			}

		}else{
			setChanged();
			notifyObservers(null);
		}
	}

	public static String getAddress(GeoPoint gp){
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

}
