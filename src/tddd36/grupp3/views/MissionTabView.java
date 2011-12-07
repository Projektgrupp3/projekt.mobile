package tddd36.grupp3.views;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.misc.NetworkManager;
import tddd36.grupp3.reports.VerificationReportActivity;
import tddd36.grupp3.reports.WindowReportActivity;
import tddd36.grupp3.resources.Event;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;
/**
 * MissionTabView, the inner tab structure of the Mission-tab.
 * Contains methods for populating a list view with mission history items
 * and star
 * @author Emil
 *
 */

//DIOANOIDNOIWD
public class MissionTabView extends TabActivity implements OnClickListener, OnTabChangeListener, Observer {

	public TabHost tabHost;
	TabHost.TabSpec spec;
	Resources res;

	private TextView missionheader, missiondescription, missionaddress, 
	missioneventid, missioninjuries, missionpriority, missiontypeofaccident;
	private Button changedescbtn, gotoaddressbtn, verificationreportbtn, windowreportbtn;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.missiontablayout);

		getMissionObjects();
		getReportObjects();

		// This code runs the first time the application is run. 
		MainView.missionController.setMissionView(this);

		tabHost = getTabHost();
		res = getResources();

		// add views to tab host
		spec = tabHost.newTabSpec("currentmission").setIndicator("Uppdrag").setContent(R.id.currenttab);
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("report").setIndicator("Rapporter").setContent(R.id.reporttab);
		tabHost.addTab(spec);

		Intent intent = new Intent().setClass(this, HistoryListView.class);
		spec = tabHost.newTabSpec("history").setIndicator("Historik")
		.setContent(intent);
		tabHost.addTab(spec);

		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 45;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 45;
		tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = 45;

		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);
	}
	@Override
	public void onResume(){
		super.onResume();
	}

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
		else if(tabName.equals("missionhistory")) {
			//TODO
		}
	}

	public void update(Observable observable, Object data) {
		if(data instanceof String[]){
			String [] update = (String[]) data;
			if(update.length>2)
				updateMissionView(update);
		}else if(data instanceof Event){	
			Event ev = (Event)data;
			System.out.println("I Update är antal skadade:" +ev.getNumberOfInjured());
			updateMissionView(ev);		
		}else if(data == null){
			System.out.println("Nu raderade den uppdraget ur vyn");
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


	public void updateMissionView(final Event event){
		runOnUiThread(new Runnable(){
			public void run() {
				missioneventid.setText(event.getID());
				missionpriority.setText(event.getPriority());
				missionheader.setText(event.getAccidentType());
				missionaddress.setText(""+event.getAddress());
				missioninjuries.setText(""+event.getNumberOfInjured());
				missiontypeofaccident.setText(event.getTypeOfInjury());
				missiondescription.setText(event.getDescription());
			}
		});
	}

	public void onClick(View v) {
		Intent intent;
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		if(v == gotoaddressbtn){
			GeoPoint gp = MainView.missionController.getActiveMissionAddress();   
			if(gp!=null){
				MainView.tabHost.setCurrentTab(0);
				MapGUI.controller.animateTo(gp);
			}else{
				Toast.makeText(getBaseContext(), "Du har ingen uppdrags adress att gå till.", Toast.LENGTH_SHORT).show();
			}
		}else if(v == verificationreportbtn){

			if(MainView.missionController.getActiveMission() != null){
				intent = new Intent(getParent(), VerificationReportActivity.class);
				parentActivity.startChildActivity("VerificationReport", intent);
			}
			else
				Toast.makeText(getBaseContext(), "Du har inget uppdrag att rapportera.", Toast.LENGTH_SHORT).show();

		}
		else if(v == windowreportbtn){

			if(MainView.missionController.getActiveMission() != null){
				intent = new Intent(getParent(), WindowReportActivity.class);
				parentActivity.startChildActivity("WindowReport", intent);
			}
			else
				Toast.makeText(getBaseContext(), "Du har inget uppdrag att rapportera.", Toast.LENGTH_SHORT).show();

		}
		else if(v == changedescbtn){
			if(MainView.missionController.getActiveMission() != null){
				Gson gson = new Gson();
				intent = new Intent(getParent(), UpdateMission.class);
				String missionExtra = gson.toJson(MainView.missionController.getActiveMission());
				intent.putExtra("mission", missionExtra);
				parentActivity.startChildActivity("UpdateMission", intent);
			}
			else
				Toast.makeText(getBaseContext(), "Du har inget uppdrag att ändra.", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void onBackPressed(){
		getParent().onBackPressed();
	}
}
