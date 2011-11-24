package tddd36.grupp3.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.resources.Event;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MissionTabView extends TabActivity implements OnTabChangeListener, Observer {

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
		List<String[]> historylistitems = new ArrayList<String[]>();
		historylistitems.add(new String[] {"Trafikolycka", "Fler skadade."});
		historylistitems.add(new String[] {"Trafikolycka", "Barn kan finnas med."});
		historylistitems.add(new String[] {"Trafikolycka", "Påbörjat uppdrag."});
		MissionHistoryAdapter historyAdapter = new MissionHistoryAdapter(getBaseContext(), R.layout.missionhistoryitem, historylistitems);
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


	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
	public class MissionHistoryAdapter extends ArrayAdapter<String[]>{

	        private ArrayList<String[]> items;

	        @SuppressWarnings("unchecked")
			public MissionHistoryAdapter(Context context, int textViewResourceId, List items) {
	                super(context, textViewResourceId, items);
	                this.items = (ArrayList<String[]>) items;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.missionhistoryitem, null);
	                }
	                String[] str = items.get(position);
	                if (str != null) {
	                        TextView tt = (TextView) v.findViewById(R.id.mission);
	                        TextView bt = (TextView) v.findViewById(R.id.change);
	                        if (tt != null) {
	                              tt.setText("Händelse: "+str[0]);                            }
	                        if(bt != null){
	                              bt.setText("Ändring: "+ str[1]);
	                        }
	                }
	                return v;
	        }
	}
}