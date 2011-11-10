package tddd36.grupp3.views;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.MapController;
import tddd36.grupp3.models.MapObjectList;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.Hospital;
import tddd36.grupp3.resources.Vehicle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapGUI extends MapActivity implements Observer {
	long pressStart;
	long pressStop;
	private static final CharSequence[] points = {"Fordon", "Sjukhus","Händelse"};
	int x, y,lat = 0, lon = 0, i=0;

	MapView map;
	MapController mapcontroller;

	static List<Overlay> overlayList;
	GeoPoint touchedPoint;
	MyLocationOverlay compass;
	com.google.android.maps.MapController controller;
	LocationManager lm;
	AlertDialog eventinfo,logout; 
	Geocoder geocoder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		

		map = (MapView)findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);

		TouchOverlay t = new TouchOverlay();
		overlayList = map.getOverlays();
		overlayList.add(t);		
		compass = new MyLocationOverlay(MapGUI.this, map);
		overlayList.add(compass);
		controller = map.getController();
		geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

		controller.setZoom(15);

		mapcontroller = new MapController(MapGUI.this);
	}

	public void update(Observable observable, Object data) {
		if(data instanceof MapObjectList){
			overlayList.add((MapObjectList) data);
		}
		if(data instanceof GeoPoint){
			controller.animateTo((GeoPoint) data);
		}
		map.postInvalidate();
	}
	@Override
	protected void onPause() {
		compass.disableCompass();
		super.onPause();
		mapcontroller.getLocationManager().removeUpdates(mapcontroller.getMapModel());		
	}

	@Override
	protected void onResume() {
		compass.enableCompass();
		super.onResume();
		//30000 = 5 min, 5000 = 5 kilometers
		mapcontroller.getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 5000, mapcontroller.getMapModel());
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
			startActivity(new Intent(getBaseContext(), tddd36.grupp3.models.SettingsModel.class));	
			return true;
		case R.id.status:
			//TODO
			return true;
		case R.id.voicecall:
			startActivity(new Intent(getBaseContext(), tddd36.grupp3.views.SIPView.class));	
			return true;
		case R.id.eventinfo:
			eventinfo = new AlertDialog.Builder(MapGUI.this).create();
			eventinfo.setTitle("Aktuellt larm");
			eventinfo.setMessage("Här ska det komma larminformation.");
			eventinfo.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					eventinfo.dismiss();
				}
			});
			eventinfo.show();
			return true;
		case R.id.activity:
			//TODO
			return true;
		case R.id.logout:
			logout = new AlertDialog.Builder(MapGUI.this).create();
			logout.setMessage("Är du säker på att du vill avsluta?");
			logout.setButton("Ja", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					finish();
				}
			});
			logout.setButton2("Nej", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					logout.dismiss();					
				}
			});	
			logout.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class TouchOverlay extends Overlay{
		
		AlertDialog.Builder builder;
		AlertDialog alert;

		public boolean onTouchEvent(MotionEvent e, MapView m){
			if (e.getAction() == MotionEvent.ACTION_DOWN){
				pressStart = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = map.getProjection().fromPixels(x,y);
			}
			if (e.getAction() == MotionEvent.ACTION_UP){
				pressStop = e.getEventTime();
			}
			if (pressStop - pressStart > 200){
				if(Math.abs(e.getX()-x)<10 && (Math.abs(e.getY()-y)<10)){ //Tillåter att användaren rör sitt finger lite
					builder = new AlertDialog.Builder(m.getContext());				
					alert = builder.create();

					alert.setTitle("Kartmeny");
					alert.setMessage("Välj något av nedanstående val:");
					alert.setButton("Placera en markör", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							builder.setTitle("Välj ett objekt:");
							builder.setItems(points, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch(which){
									case 0: 
										mapcontroller.addMapObject(new Vehicle(touchedPoint,"Ambulans", "Här kommer ambulansen", 2));
										return;
									case 1:
										mapcontroller.addMapObject(new Hospital(touchedPoint,"Sjukhus", "Här är ett sjukhus", 20));
										return;
									case 2:
										mapcontroller.addMapObject(new Event(touchedPoint,"Händelse", "Här är en händelse", new SimpleDateFormat("HH:mm:ss").format(new Date())));
										return;
									}								
								}
							});
							builder.show();					
						}
					});
					alert.setButton3("Hämta adress", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
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
								//no-op
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
			}
			return false;
		}
	}
}
