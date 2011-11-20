package tddd36.grupp3.views;

import java.text.ParseException;

import tddd36.grupp3.R;
import tddd36.grupp3.database.ClientDatabaseManager;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.MapObject;
import tddd36.grupp3.resources.Vehicle;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.TabActivity;
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
import com.google.android.maps.GeoPoint;

public class MainView extends TabActivity implements OnTabChangeListener{
	TabHost tabHost;

	public static ClientDatabaseManager db;
	public static SipManager manager = null;
	public static SipProfile me = null;
	public IncomingCallReceiver callReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Sipstuff
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.SipDemo.INCOMING_CALL");
		callReceiver = new IncomingCallReceiver();
		this.registerReceiver(callReceiver, filter);
		initializeManager();
		
		//this.deleteDatabase("client_database"); //KÖR DETTA OM GJORT ÄNDRINGAR I DB-koden.
		db = new ClientDatabaseManager(this);
//		db.addRow(new Contact("Emil", "bayhill@iptel.org"));
//		db.addRow(new Contact("Thomas", "thomas@domain.org"));
//		db.addRow(new Contact("Patrik","patrik@iptel.org"));
		//        db.addRow(new Vehicle(new GeoPoint(1929393838,2100101901),"Hej","hå",5));
		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, MapGUI.class);
		spec = tabHost.newTabSpec("map").setIndicator("Karta",
				res.getDrawable(R.drawable.ic_tab_menu_item))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, MissionView.class);
		spec = tabHost.newTabSpec("mission").setIndicator("Uppdrag",
				res.getDrawable(R.drawable.ic_tab_menu_item))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SIPView.class);
		spec = tabHost.newTabSpec("call").setIndicator("Samtal",
				res.getDrawable(R.drawable.ic_tab_menu_item))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);
	}

	public void onTabChanged(String arg0) {
		Activity MyActivity = this.getCurrentActivity();
		if(MyActivity instanceof MissionView){

		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		db.close();
	}
	public void switchTab(int index){
		tabHost.setCurrentTab(index);
	}
	
    public void initializeManager() {
    	if(manager == null) {
    		manager = SipManager.newInstance(this);
    	}
    	initializeLocalProfile();
    }
    
	public void initializeLocalProfile() {
    	if (manager == null) {
    		return;
    	}

    	if (me != null) {
    		closeLocalProfile();
    	}

    	try {
    		SipProfile.Builder builder = new SipProfile.Builder("mrananas","ekiga.net");
    		builder.setPassword("password");
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
