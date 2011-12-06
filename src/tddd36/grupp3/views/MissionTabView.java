package tddd36.grupp3.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONObject;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.misc.SplashEvent;
import tddd36.grupp3.reports.VerificationReportActivity;
import tddd36.grupp3.reports.WindowReportActivity;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.Event;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;

import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;
/**
 * MissionTabView, the inner tab structure of the Mission-tab.
 * Contains methods for populating a list view with mission history items
 * and star
 * @author Emil
 *
 */
public class MissionTabView extends TabActivity implements OnClickListener, OnTabChangeListener, Observer {

	public static TabHost tabHost;
	TabHost.TabSpec spec;
	Resources res;

	public static MissionController mc;
	private TextView missionheader, missiondescription, missionaddress, 
	missioneventid, missioninjuries, missionpriority, missiontypeofaccident;
	private Button changedescbtn, gotoaddressbtn, verificationreportbtn, windowreportbtn;
	private String[] mission;
	private ListView listView;
	private ScrollView view;
	private ArrayList<String> textViewArray;
	private ArrayList<String[]> historylistitems;
	private MissionHistoryAdapter historyAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.missiontablayout);

		getMissionObjects();
		getHistoryObjects();
		getReportObjects();

		mc = new MissionController(MissionTabView.this);

		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		res = getResources();

		// setup the list view
		listView = (ListView) findViewById(R.id.historylist);
		listView.setEmptyView((TextView) findViewById(R.id.empty));

		// some dummy strings to the list
		historylistitems = new ArrayList<String[]>();
		historylistitems.add(new String[] {"Trafikolycka", "Fler skadade."});
		historylistitems.add(new String[] {"Trafikolycka", "Barn kan finnas med."});
		historylistitems.add(new String[] {"Trafikolycka", "Påbörjat uppdrag."});
		historyAdapter = new MissionHistoryAdapter(getBaseContext(), R.layout.missionhistoryitem, historylistitems);
		listView.setAdapter(historyAdapter);
		// add views to tab host
		spec = tabHost.newTabSpec("currentmission").setIndicator("Uppdrag").setContent(R.id.currenttab);
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("report").setIndicator("Rapporter").setContent(R.id.reporttab);
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("history").setIndicator("Historik").setContent(
				new TabContentFactory() {
					public View createTabContent(String arg0) {
						return listView;
					}
				});
		tabHost.addTab(spec);

		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 45;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 45;
		tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = 45;

		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);

	}

//	public void addHistoryItem(String header, String change ){
//		final String [] newItem = {header, change};
//		runOnUiThread(new Runnable(){
//			public void run(){
//				historylistitems.add(newItem);
//				historyAdapter.notifyDataSetChanged();
//			}
//		});
//	}
	private void getMissionObjects() {		
		missioneventid = (TextView)findViewById(R.id.eventID2);
		missionpriority = (TextView)findViewById(R.id.eventPriority2);
		missionheader = (TextView)findViewById(R.id.missionheader2);
		missionaddress = (TextView)findViewById(R.id.missionaddress2);
		missioninjuries = (TextView)findViewById(R.id.missioninjuries2);
		missiontypeofaccident = (TextView)findViewById(R.id.typeOfAccident2);
		missiondescription = (TextView)findViewById(R.id.missiondescription2);

		gotoaddressbtn = (Button)findViewById(R.id.gotoaddressbtn);
		gotoaddressbtn.setOnClickListener(this);
		changedescbtn = (Button)findViewById(R.id.changedescbtn);
		changedescbtn.setOnClickListener(this);

	}

	private void getHistoryObjects() {
		// TODO Auto-generated method stub
	}

	private void getReportObjects() {
		verificationreportbtn = (Button)findViewById(R.id.verificationreportbtn);
		verificationreportbtn.setOnClickListener(this);
		windowreportbtn = (Button)findViewById(R.id.windowreportbtn);
		windowreportbtn.setOnClickListener(this);
	}

	public void onTabChanged(String tabName) {
		if(tabName.equals("currentmission")) {
			//TODO
		}
		else if(tabName.equals("misionhistory")) {
			//TODO
		}
	}

	public void update(Observable observable, Object data) {
		if(data instanceof String[]){
			final String [] update = (String[]) data;
			if(update.length>2){
				updateMissionView(update);
			}
			else{
				runOnUiThread(new Runnable(){
					public void run(){
						Log.d(update[0],update[1]);
						historylistitems.add(update);
						historyAdapter.notifyDataSetChanged();
					}
				});
			}
		}else if(data instanceof Event){
//			runOnUiThread(new Runnable(){
//				public void run(){
//					Log.d("snopp","ananas");
//					historylistitems.add(new String[]{"hej", "hå"});
//					historyAdapter.notifyDataSetChanged();
//				}
//			});
			updateMissionView((Event) data);			
		}else if(data == null){
			clearMissionView();
		}
	}
	private void clearMissionView() {
		missioneventid.setText("Tomt");
		missionpriority.setText("Tomt");
		missionheader.setText("Tomt");
		missionaddress.setText("Tomt");
		missioninjuries.setText("Tomt");
		missiontypeofaccident.setText("Tomt");
		missiondescription.setText("Tomt");
	}
	public void updateMissionView(String[] missiontext){
		missioneventid.setText(missiontext[0]);
		missionpriority.setText(missiontext[1]);
		missionheader.setText(missiontext[2]);
		missionaddress.setText(missiontext[3]);
		missioninjuries.setText(missiontext[4]);
		missiontypeofaccident.setText(missiontext[5]);
		missiondescription.setText(missiontext[6]);
	}


	public void updateMissionView(Event event){
		missioneventid.setText(event.getID());
		missionpriority.setText(event.getPriority());
		missionheader.setText(event.getAccidentType());
		missionaddress.setText(""+event.getAddress());
		missioninjuries.setText(""+event.getNumberOfInjured());
		missiontypeofaccident.setText(event.getTypeOfInjury());
		missiondescription.setText(event.getDescription());
	}
	
	public void fireSplashEvent(JSONObject messageFromServer){
		Intent splashIntent = new Intent(MainView.context, SplashEvent.class);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		splashIntent.putExtra("json", messageFromServer.toString());
		parentActivity.startChildActivity("IncomingEvent", splashIntent);
	}
	


	public void onClick(View v) {
		Intent intent;
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		if(v == gotoaddressbtn){
			GeoPoint gp = mc.getActiveMissionAddress();   
			if(gp!=null){
				MainView.tabHost.setCurrentTab(0);
				MapGUI.controller.animateTo(gp);
			}else{
				Toast.makeText(getBaseContext(), "Du har inget uppdrag.", Toast.LENGTH_SHORT).show();
			}
		}else if(v == verificationreportbtn){
			intent = new Intent(getParent(), VerificationReportActivity.class);
			parentActivity.startChildActivity("VerificationReport", intent);

		}else if(v == windowreportbtn){
			intent = new Intent(getParent(), WindowReportActivity.class);
			parentActivity.startChildActivity("WindowReport", intent);

		}else if(v == changedescbtn){
			if(MissionController.getActiveMission() != null){
				Gson gson = new Gson();
				intent = new Intent(getParent(), UpdateMission.class);
				String missionExtra = gson.toJson(MissionController.getActiveMission());
				intent.putExtra("mission", missionExtra);
				parentActivity.startChildActivity("UpdateMission", intent);
			}
			else
				Toast.makeText(getBaseContext(), "Du har inget uppdrag att ändra.", Toast.LENGTH_SHORT).show();
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
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void onBackPressed(){

	}
	/**
	 * Anonymous inner class for filling the mission history list with history items
	 * @author Emil
	 *
	 */
	public class MissionHistoryAdapter extends ArrayAdapter<String[]>{

		private	List<String[]> items;
		private TextView historyHeader, historyChange;
		
		public int getCount() {
			return this.items.size();
		}

		public String[] getItem(int index) {
			return this.items.get(index);
		}
		
		@SuppressWarnings("unchecked")
		public MissionHistoryAdapter(Context context, int textViewResourceId, List items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		/**
		 * Automatically called by the list activity when populating the list with historyevents.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.missionhistoryitem, parent, false);
			}
			
			// Get item
			String[] historyItems = getItem(position);

			// Get reference to TextView - contactname
			historyHeader = (TextView) v.findViewById(R.id.historyHeader);

			// Get reference to TextView - contactaddress
			historyChange = (TextView) v.findViewById(R.id.historyChange);
			//Set contact name
			historyHeader.setText("Händelse: "+historyItems[0]);

			// Set contact sip address
			historyChange.setText("Ändring: "+historyItems[1]);
			
			return v;
		}
	}
}
