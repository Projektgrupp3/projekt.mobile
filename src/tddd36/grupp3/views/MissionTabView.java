package tddd36.grupp3.views;

import java.util.ArrayList;
import java.util.List;

import tddd36.grupp3.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MissionTabView extends TabActivity implements OnTabChangeListener {

	public static TabHost tabHost;
	TabHost.TabSpec spec;
	Resources res;

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.missiontablayout);

		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		res = getResources();

		// setup the list view
		listView = (ListView) findViewById(R.id.historylist);
		listView.setEmptyView((TextView) findViewById(R.id.empty));

		// some dummy strings to the list
		List<String> historylistitems = new ArrayList<String>();
		historylistitems.add("Ändring nummer 1");
		historylistitems.add("Ändring nummer 2");
		historylistitems.add("Ändring nummer 3");
		ArrayAdapter<String> historyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historylistitems);
		listView.setAdapter(historyAdapter);

		Intent intent = new Intent().setClass(this, MissionView.class);
		spec = tabHost.newTabSpec("currentmission").setIndicator("Aktuellt uppdrag",
				null)
				.setContent(intent);
		tabHost.addTab(spec);

		// add views to tab host
		spec = tabHost.newTabSpec("history").setIndicator("Uppdragshistorik").setContent(
				new TabContentFactory() {
					public View createTabContent(String arg0) {
						return listView;
					}
				});
		tabHost.addTab(spec);	

		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 45;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 45;

		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);
	}


	/**
	 * Implement logic here when a tab is selected
	 */
	public void onTabChanged(String tabName) {
		if(tabName.equals("currentmission")) {
			//TODO
		}
		else if(tabName.equals("misionhistory")) {
			//TODO
		}
	}
	public void onBackPressed(){
		//NO-OP
	}
}