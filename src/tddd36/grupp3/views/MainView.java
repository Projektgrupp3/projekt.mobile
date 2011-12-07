package tddd36.grupp3.views;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.controllers.ConnectionController;
import tddd36.grupp3.controllers.MapController;
import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.database.ClientDatabaseManager;
import tddd36.grupp3.misc.NetworkManager;
import tddd36.grupp3.misc.QoSManager;
import tddd36.grupp3.misc.SplashEvent;
import tddd36.grupp3.models.MapModel;
import tddd36.grupp3.models.MissionModel;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.Status;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
/**
 * TabActivity for showing the Tab-structure of application. 
 * Also contains some support calls for intializing the SIP-part of the
 * application and the Profile, Manager and CallReciever.
 * @author Projektgrupp 3 - Sjukvï¿½rden
 *
 */
public class MainView extends TabActivity implements OnTabChangeListener{
	public static TabHost tabHost;
	TabHost.TabSpec spec;	
	Resources res;

	public static Context context;

	private static String user;
	private static String pass;

	public static ClientDatabaseManager db;
	public static SipManager manager = null;
	public static SipProfile me = null;
	public IncomingCallReceiver callReceiver;

	public static MapController mapController;
	public static MissionController missionController;

	public static WindowManager.LayoutParams lp;

	private AlertDialog logout;
	public Toast statusMissionAlert;

	public static QoSManager QoSManager;
	/**
	 * OnCreate-method setting up the tab structure via the static TabHost. 
	 * Also intializes the SQLite database containing map objects, the users current mission and
	 * contacts.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		NetworkManager.chkStatus(MainView.this);

		//this.deleteDatabase("client_database"); //Kï¿½R DETTA OM GJORT ï¿½NDRINGAR I DB-koden.
		db = new ClientDatabaseManager(this);

		user = getIntent().getExtras().getString("user");
		pass = getIntent().getExtras().getString("pass");

		mapController = new MapController(MainView.this);
		missionController = new MissionController(MainView.this);
		//missionController.setActiveMission(getCurrentMissionFromDB());

		context = getBaseContext();

		// Sipstuff
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.SipDemo.INCOMING_CALL");
		callReceiver = new IncomingCallReceiver();
		this.registerReceiver(callReceiver, filter);
		initializeManager();

		res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost();  // The activity TabHost
		//TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, MapGUI.class);
		spec = tabHost.newTabSpec("map").setIndicator("Karta",
				res.getDrawable(R.drawable.map_tab_icon))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, MissionGroupActivity.class);
		spec = tabHost.newTabSpec("mission").setIndicator("Uppdrag",
				res.getDrawable(R.drawable.event_tab_icon))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SIPGroupActivity.class);
		spec = tabHost.newTabSpec("contacts").setIndicator("Kontakter",
				res.getDrawable(R.drawable.contact_tab_icon))
				.setContent(intent);
		tabHost.addTab(spec);

		Sender.send(Sender.REQ_ALL_CONTACTS);

		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);
		QoSManager = new QoSManager(getWindow().getAttributes(), this);
		this.registerReceiver(QoSManager.myBatteryReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	@SuppressWarnings("unchecked")
	public Event getCurrentMissionFromDB() {
		ArrayList<Event> events = MainView.db.getAllRowsAsArrayList("mission");
		if(events.size() > 0){
			if(events.get(0) != null){
				return events.get(0);
			}
		}
		return null;
	}

	/**
	 * Dummy-method, does not actually do anything at the moment.
	 */
	public void onTabChanged(String arg0) {

	}
	/**
	 * Called when some instance calls getParent().finish(). 
	 * Closes the LocalProfile for SIP and closes the database.
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();
		System.out.println("nu är den i mainviews ondestroy.");
		unregisterReceiver(callReceiver);
		unregisterReceiver(QoSManager.myBatteryReceiver);
		closeLocalProfile();
		Sender.send(Sender.LOG_OUT);
		try {
			ConnectionController.serversocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		db.close();
	}
	/**
	 * Method for initializing the SipManager and calls for a new LocalProfile
	 */
	public void initializeManager() {
		if(manager == null) {
			manager = SipManager.newInstance(this);
		}
		initializeLocalProfile();
	}
	/**
	 * Method for initializing the SIP LocalProfile.
	 */
	public void initializeLocalProfile() {
		if (manager == null) {
			return;
		}

		if (me != null) {
			closeLocalProfile();
		}

		try {
			SipProfile.Builder builder = new SipProfile.Builder(user,"ekiga.net");
			builder.setPassword(pass.replaceFirst("[0-9]", ""));
			me = builder.build();
			Intent i = new Intent();
			i.setAction("android.SipDemo.INCOMING_CALL");
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, Intent.FILL_IN_DATA);
			manager.open(me, pi, null);

		} catch (ParseException pe) {
			//updateStatus("Connection Error.");
		} catch (SipException se) {
			//updateStatus("Connection error.");
		}
	}

	/**
	 * Method for closing the LocalProfile.
	 */
	public void closeLocalProfile() {
		if (manager == null) {
			return;
		}
		try {
			if (me != null) {
				manager.close(me.getUriString());
			}
		} catch (Exception ee) {
			Log.d("WalkieTalkieActivity/onDestroy", "Failed to close local profile.", ee);
		}
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		boolean hasActiveMission = MissionController.hasActiveMission();
		if(hasActiveMission){
			statusMissionAlert = Toast.makeText(this, "Status: "+item.getTitle(), Toast.LENGTH_SHORT);
		} else{
			statusMissionAlert = Toast.makeText(this, "Inget aktivt uppdrag", Toast.LENGTH_SHORT);
		}
		switch (item.getItemId()) {

		case R.id.settings:
			startActivity(new Intent(getBaseContext(), tddd36.grupp3.views.SettingsView.class));	
			return true;
		case R.id.status:
			return true;
		case R.id.recieved:
			if(hasActiveMission){
				MissionModel.setStatus(Status.RECIEVED);
				Sender.send(Sender.ACK_STATUS+":"+Status.RECIEVED.toString()+":"+
						"Händelse-ID: "+MainView.missionController
						.getActiveMission().getID());
			}
			statusMissionAlert.show();
			return true;
		case R.id.there:
			if(hasActiveMission){
				MissionModel.setStatus(Status.THERE);
				Sender.send(Sender.ACK_STATUS+":"+Status.THERE.toString()+":"+
						"Händelse-ID: "+MainView.missionController
						.getActiveMission().getID());
			}
			statusMissionAlert.show();
			return true;
		case R.id.loaded:
			if(hasActiveMission){
				MissionModel.setStatus(Status.LOADED);
				Sender.send(Sender.ACK_STATUS+":"+Status.LOADED.toString()+":"+
						"Händelse-ID: "+MainView.missionController
						.getActiveMission().getID());
			}
			statusMissionAlert.show();
			return true;
		case R.id.depart:	
			MissionModel.setStatus(Status.DEPART);
			Sender.send(Sender.ACK_STATUS+":"+Status.DEPART.toString()+":"+
					"Händelse-ID: "+MainView.missionController
					.getActiveMission().getID());
			statusMissionAlert.show();
			return true;
		case R.id.home:
			if(hasActiveMission){
				builder.setTitle("Verifiera..");
				builder.setMessage("Vill du avsluta ditt nuvarande uppdrag?");
				builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Sender.send(Sender.ACK_STATUS+":"+Status.HOME.toString()+":"+
								"Händelse-ID: "+MainView.missionController
								.getActiveMission().getID());
						MainView.missionController.setActiveMission(null);
						MissionModel.setStatus(Status.HOME);
						statusMissionAlert.show();
					}});
				builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}});
				builder.show();
			}
			return true;
		case R.id.centeratme:
			MapGUI.myLocation = mapController.fireCurrentLocation();
			if(MapGUI.myLocation!=null){
				MapGUI.controller.setZoom(15);
				MapGUI.controller.animateTo(MapGUI.myLocation);
			}else{
				Toast.makeText(getBaseContext(), MapModel.GPS_FAILED, Toast.LENGTH_SHORT).show();
			}			
			return true;
		case R.id.logout:
			logout = new AlertDialog.Builder(this).create();
			logout.setMessage("Ã„r du sÃ¤ker pÃ¥ att du vill avsluta?");
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
}
