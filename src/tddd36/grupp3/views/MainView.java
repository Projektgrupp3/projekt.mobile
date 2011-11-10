package tddd36.grupp3.views;

import tddd36.grupp3.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainView extends TabActivity implements OnTabChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MapGUI.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("Map").setIndicator("Karta",
                          res.getDrawable(R.drawable.ic_tab_menu_item))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, EventView.class);
        spec = tabHost.newTabSpec("event").setIndicator("Händelse",
                          res.getDrawable(R.drawable.ic_tab_menu_item))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SIPView.class);
        spec = tabHost.newTabSpec("Call").setIndicator("Samtal",
                          res.getDrawable(R.drawable.ic_tab_menu_item))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(1);
    }

	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
