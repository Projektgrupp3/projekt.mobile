package tddd36.grupp3.views;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import org.json.JSONException;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.database.ClientDatabaseManager;
import tddd36.grupp3.misc.SplashEvent;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.Event;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
/**
 * TabActivity for showing the Tab-structure of application. 
 * Also contains some support calls for intializing the SIP-part of the
 * application and the Profile, Manager and CallReciever.
 * @author Projektgrupp 3 - Sjukvården
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
	/**
	 * OnCreate-method setting up the tab structure via the static TabHost. 
	 * Also intializes the SQLite database containing map objects, the users current mission and
	 * contacts.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		user = getIntent().getExtras().getString("user");
		pass = getIntent().getExtras().getString("pass");
		
		context = getBaseContext();

		// Sipstuff
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.SipDemo.INCOMING_CALL");
		callReceiver = new IncomingCallReceiver();
		this.registerReceiver(callReceiver, filter);
		initializeManager();

		this.deleteDatabase("client_database"); //KÖR DETTA OM GJORT ÄNDRINGAR I DB-koden.
		db = new ClientDatabaseManager(this);
//		db.addRow(new Contact("Enhet 1","enhet1@ekiga.net"));
//		db.addRow(new Contact("Enhet 2", "enhet2@ekiga.net"));
//		db.addRow(new Contact("Enhet 3", "enhet3@ekiga.net"));
//		db.addRow(new Contact("Emil", "bayhill@ekiga.net"));

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

			try {
				Sender.send("getContacts");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);
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
		unregisterReceiver(callReceiver);
		closeLocalProfile();
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
}
