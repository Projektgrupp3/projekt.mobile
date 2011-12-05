package tddd36.grupp3.views;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.controllers.ConnectionController;
import tddd36.grupp3.controllers.MapController;
import tddd36.grupp3.misc.NetworkManager;
import tddd36.grupp3.models.MapModel;
import tddd36.grupp3.models.MapObjectList;
import tddd36.grupp3.models.MissionModel;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.FloodEvent;
import tddd36.grupp3.resources.MapObject;
import tddd36.grupp3.resources.OtherEvent;
import tddd36.grupp3.resources.RoadBlockEvent;
import tddd36.grupp3.resources.Status;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
/***
 * The MapView tab. Contains methods for drawing map and map objects onto the map itself. 
 * @author Projektgrupp 3 - Sjukvården
 *
 */
public class MapGUI extends MapActivity implements Observer {

	private long pressStart;
	private long pressStop;
	private static final CharSequence[] points = {"Hinder på vägen", "Översvämning","Fritext"};
	private int x, y,lat = 0, lon = 0;
	private Event o;

	private MapView map;
	public static MapController mapcontroller;

	private Drawable d;

	static List<Overlay> overlayList;
	GeoPoint touchedPoint, myLocation;
	MyLocationOverlay compass;
	public static com.google.android.maps.MapController controller;
	AlertDialog eventinfo,logout; 
	Geocoder geocoder;
	/**
	 * onCreate for MapGUI 
	 * Sets up the MapView and initiates MapController.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);

		d = getResources().getDrawable(R.drawable.pinpoint);

		map = (MapView)findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);

		TouchOverlay t = new TouchOverlay();
		overlayList = map.getOverlays();
		overlayList.add(t);		
		compass = new MyLocationOverlay(MapGUI.this, map);

		overlayList.add(compass);

		controller = map.getController();
		geocoder = new Geocoder(getBaseContext(), Locale.getDefault());		

		mapcontroller = new MapController(MapGUI.this);
		if((myLocation = mapcontroller.fireCurrentLocation()) != null){
			controller.animateTo(myLocation);
		}
		controller.setZoom(15);
	}
	/**
	 * Called by Observable MapModel. Adds map objects to the overlaylist and
	 * to correct mapobjectlist, also animates to geopoints.
	 */
	public void update(Observable observable, Object data) {

		if(data instanceof MapObjectList){
			overlayList.add((MapObjectList) data);

			if(data instanceof OverlayItem){
				controller.animateTo(((OverlayItem) data).getPoint());
			}
			if(data instanceof GeoPoint){
				controller.animateTo((GeoPoint) data);
			}
		}else if(data instanceof MapObject[]){
			for(MapObject o: (MapObject[]) data){
				if(o != null){
					mapcontroller.addMapObject(o);
				}
			}
		}else if(data == null){
			Toast.makeText(getBaseContext(), "Ett fel uppstod vid \n tilläggning av objekt.", Toast.LENGTH_SHORT).show();
		}
		map.postInvalidate();
	}
	/**
	 * Called when the application is paused.
	 * Disables compass and removes the MapModel from the LocationManager in MapController
	 */
	@Override
	protected void onPause() {
		compass.disableCompass();
		compass.disableMyLocation();
		super.onPause();
		mapcontroller.getLocationManager().removeUpdates(mapcontroller.getMapModel());		
	}
	/**
	 * Called when application is resumed
	 * Enables compass and enables LocationManager setting the update variables as follows:
	 * Update every 30000 sec = 5 min or 5000 meters = 5 kilometers
	 */
	@Override
	protected void onResume() {
		compass.enableCompass();
		compass.enableMyLocation();
		super.onResume();
		mapcontroller.getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 5000, mapcontroller.getMapModel());
	}

	/**
	 * Called when hardware "menu-button" is pressed.
	 * Inflates the mainmenu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	/**
	 * Called when an item is selected in the options menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.settings:
			startActivity(new Intent(getBaseContext(), tddd36.grupp3.views.SettingsView.class));	
			return true;
		case R.id.status:
			return true;
		case R.id.recieved:
			item.setChecked(true);
			MissionModel.setStatus(Status.RECIEVED);
			Sender.send(Sender.ACK_STATUS+":"+Status.RECIEVED.toString());
			return true;
		case R.id.there:
			item.setChecked(true);
			MissionModel.setStatus(Status.THERE);
			Sender.send(Sender.ACK_STATUS+":"+Status.THERE.toString());
			return true;
		case R.id.loaded:
			item.setChecked(true);
			MissionModel.setStatus(Status.LOADED);
			Sender.send(Sender.ACK_STATUS+":"+Status.LOADED.toString());
			return true;
		case R.id.depart:			
			item.setChecked(true);
			MissionModel.setStatus(Status.DEPART);
			Sender.send(Sender.ACK_STATUS+":"+Status.DEPART.toString());
			return true;
		case R.id.home:
			item.setChecked(true);
			MissionModel.setStatus(Status.HOME);
			Sender.send(Sender.ACK_STATUS+":"+Status.HOME.toString());
			return true;
		case R.id.centeratme:
			myLocation = mapcontroller.fireCurrentLocation();
			if(myLocation!=null){
				controller.setZoom(15);
				controller.animateTo(myLocation);
			}else{
				Toast.makeText(getBaseContext(), MapModel.GPS_FAILED, Toast.LENGTH_SHORT).show();
			}			
			return true;
		case R.id.logout:
			logout = new AlertDialog.Builder(MapGUI.this).create();
			logout.setMessage("Är du säker på att du vill avsluta?");
			logout.setButton("Ja", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					getParent().finish();
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
	/**
	 * Default dummy-method for Google Maps, does nothing.
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Anonymous inner class for handling touch events on the map
	 * Contains methods for evaulating how long the touch was
	 * and draws a map menu when user touch was longer than 200ms
	 * @author Projektgrupp 3
	 *
	 */
	class TouchOverlay extends Overlay{

		AlertDialog.Builder builder;
		AlertDialog alert;
		EditText input1;
		EditText input2;
		View twoEdits;

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
				if(Math.abs(e.getX()-x)<10 && (Math.abs(e.getY()-y)<10)){ //Tillåter att användaren "darrar" på handen.
					builder = new AlertDialog.Builder(m.getContext());				
					alert = builder.create();
					alert.setTitle("Kartmeny");
					alert.setMessage("Välj något av nedanstående val:");
					alert.setButton("Placera en händelse", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							NetworkManager.chkStatus(MapGUI.this);
							builder.setTitle("Välj en händelse:");
							builder.setItems(points, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch(which){
									case 0: 
										try {
											RoadBlockEvent newEvent = new RoadBlockEvent(touchedPoint,points[0].toString(), "Ett föremål på vägen förhindrar trafik från att komma fram", 
													new SimpleDateFormat("yyMMddHHmmss").format(new Date()), R.drawable.road_closed_icon);
											Sender.send(newEvent);
											mapcontroller.addMapObject(newEvent);
										} catch (JSONException e) {
											e.printStackTrace();
										}
										break;
									case 1:
										try {
											FloodEvent newEvent = new FloodEvent(touchedPoint,points[1].toString(), "Det är en översväming på platsen",
													new SimpleDateFormat("yyMMddHHmmss").format(new Date()), R.drawable.flood_icon);
											Sender.send(newEvent);
											mapcontroller.addMapObject(newEvent);
										} catch (JSONException e) {
											e.printStackTrace();
										}
										break;
									case 2:
										AlertDialog.Builder createCustomEventDialog = new AlertDialog.Builder(MapGUI.this);

										createCustomEventDialog.setTitle("Lägg till händelse");

										LinearLayout lila1 = new LinearLayout(MapGUI.this);
										lila1.setOrientation(1); //1 is for vertical orientation
										final EditText input1 = new EditText(MapGUI.this); 
										final EditText input2 = new EditText(MapGUI.this);
										final TextView header = new TextView(MapGUI.this);
										final TextView desc = new TextView(MapGUI.this);

										header.setText("Rubrik");
										desc.setText("Beskrivning");

										lila1.addView(header);
										lila1.addView(input1);
										lila1.addView(desc);
										lila1.addView(input2);
										createCustomEventDialog.setView(lila1);

										createCustomEventDialog.setPositiveButton("Lägg till", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												try {
													OtherEvent newEvent = new OtherEvent(touchedPoint, input1.getText().toString(), 
															input2.getText().toString(),
															new SimpleDateFormat("yyMMddHHmmss").format(new Date()), R.drawable.green_flag_icon);
													Sender.send(newEvent);
													mapcontroller.addMapObject(newEvent);
												} catch (JSONException e) {
													e.printStackTrace();
												}

											}
										});

										createCustomEventDialog.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {

											}
										});
										createCustomEventDialog.show();
										break;
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
										display += address.get(0).getAddressLine(i);
									}
									Toast t = Toast.makeText(getBaseContext(), display, Toast.LENGTH_LONG);
									t.show();
								}
							} catch (IOException e) {

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

	/**
	 * Called when the hardware "back"-button was pressed. 
	 * Pops a dialog asking the user if it wants to log out.
	 */
	public void onBackPressed(){
		AlertDialog logout = new AlertDialog.Builder(this).create();
		logout.setMessage("Är du säker på att du vill avsluta?");
		logout.setButton("Ja", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){
				Sender.send(Sender.LOG_OUT);
				finish();
			}
		});
		logout.setButton2("Nej", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();					
			}
		});	
		logout.show();
	}
}
