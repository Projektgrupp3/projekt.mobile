package tddd36.grupp3.views;

import tddd36.grupp3.R;
import tddd36.grupp3.database.ClientDatabaseManager;
import tddd36.grupp3.resources.Contact;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainView extends TabActivity implements OnTabChangeListener{
	TabHost tabHost;
	
	public static ClientDatabaseManager db;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        this.deleteDatabase("client_database"); //KÖR DETTA OM GJORT ÄNDRINGAR I DB-koden.
        db = new ClientDatabaseManager(this);
//        db.addRow(new Contact("Emil", "bayhill@iptel.org"));
//        db.addRow(new Contact("Thomas", "thomas@domain.org"));
//        db.addRow(new Contact("Patrik","patrik@iptel.org"));

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
}
