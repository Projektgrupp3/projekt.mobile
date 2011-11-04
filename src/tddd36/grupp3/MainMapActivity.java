package tddd36.grupp3;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tddd36.grupp3.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainMapActivity extends MapActivity implements LocationListener{

	MapView map;
	long start;
	long stop;
	MyLocationOverlay compass;
	MapController controller;
	int x, y,lat = 0, lon = 0;
	GeoPoint touchedPoint;
	Drawable d;
	List<Overlay> overlayList;
	LocationManager lm;
	String towers;
	GeoPoint ourLocation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		map = (MapView)findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);

		Touchy t = new Touchy();
		overlayList = map.getOverlays();
		overlayList.add(t);		
		compass = new MyLocationOverlay(MainMapActivity.this, map);
		overlayList.add(compass);
		controller = map.getController();
		d = getResources().getDrawable(R.drawable.pinpoint);

		//Placing pinpoint at our location
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		towers = lm.getBestProvider(criteria, false);
		Location location = lm.getLastKnownLocation(towers);
		if(location != null){
			lat = (int) (location.getLatitude() * 1E6);
			lon = (int) (location.getLongitude() * 1E6);
			ourLocation = new GeoPoint(lat, lon);
			OverlayItem overlayItem = new OverlayItem(ourLocation,"Sträng 1", "Sträng 2");
			CustomPinpoint custom = new CustomPinpoint(d,MainMapActivity.this);
			custom.insertPinpoint(overlayItem);
			overlayList.add(custom);
		}else Toast.makeText(MainMapActivity.this, "Kunde inte hämta leverantör", Toast.LENGTH_SHORT).show();
		
		controller.animateTo(ourLocation);
		controller.setZoom(15);
	}

	@Override
	protected void onPause() {
		compass.disableCompass();
		super.onPause();
		lm.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		compass.enableCompass();
		super.onResume();
		lm.requestLocationUpdates(towers, 500, 1, this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay{
		public boolean onTouchEvent(MotionEvent e, MapView m){
			if (e.getAction() == MotionEvent.ACTION_DOWN){
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = map.getProjection().fromPixels(x,y);
			}
			if (e.getAction() == MotionEvent.ACTION_UP){
				stop = e.getEventTime();
				if(!((int)e.getX() == x) && !((int)e.getY() == y)){
					start = stop;
				}
			}
			if (stop - start > 200){
				AlertDialog alert = new AlertDialog.Builder(MainMapActivity.this).create();
				alert.setTitle("Kartmeny");
				alert.setMessage("Välj något av nedanstående val:");
				alert.setButton("Placera en markör", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						OverlayItem overlayItem = new OverlayItem(touchedPoint,"Sträng 1", "Sträng 2");
						CustomPinpoint custom = new CustomPinpoint(d,MainMapActivity.this);
						custom.insertPinpoint(overlayItem);
						overlayList.add(custom);

					}
				});
				alert.setButton3("Hämta adress", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
						try{
							List<Address> address = geocoder.getFromLocation(touchedPoint.getLatitudeE6() / 1E6, touchedPoint.getLongitudeE6() / 1E6, 1);
							if(address.size() > 0){
								String display = "";
								for(int i = 0;i<address.get(0).getMaxAddressLineIndex();i++){
									display += address.get(0).getAddressLine(i) + "\n";
								}
								Toast t = Toast.makeText(getBaseContext(), display, Toast.LENGTH_LONG);
								t.show();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}finally{

						}
					}
				});
				alert.setButton2("Satellit/Karta", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if(map.isSatellite()){
							map.setSatellite(false);
						}else{
							map.setSatellite(true);
						}

					}
				});
				alert.show();
				return true;
			}
			return false;
		}
	}
	/** 
	 * Kallas på när location uppdateras.
	 */
	public void onLocationChanged(Location l) {
		// TODO Auto-generated method stub
		lat = (int) (l.getLatitude() * 1E6);
		lon = (int) (l.getLongitude() * 1E6);
		GeoPoint ourLocation = new GeoPoint(lat, lon);
		OverlayItem overlayItem = new OverlayItem(ourLocation,"Sträng 1", "Sträng 2");
		CustomPinpoint custom = new CustomPinpoint(d,MainMapActivity.this);
		custom.insertPinpoint(overlayItem);
		overlayList.add(custom);
	}
	/**
	 * Kallas på när hårdvaru-meny-knappen trycks in
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu:
	    	startActivity(new Intent(getBaseContext(), tddd36.grupp3.SettingsActivity.class));	
	        return true;
	    case R.id.status:
	        //TODO
	        return true;
	    case R.id.voicecall:
	    	//TODO
	    	return true;
	    case R.id.eventinfo:
	    	//TODO
	    	return true;
	    case R.id.activity:
	    	//TODO
	    	return true;
	    case R.id.logout:
	    
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
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
}