package tddd36.grupp3.views;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.MissionController;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class MissionView extends Activity implements Observer, OnClickListener{

	public static MissionController mc;
	private TextView missionheader, missiondescription, missionaddress, 
	missioneventid, missioninjuries, missionpriority, missiontypeofaccident;
	private Button changedescbtn, gotoaddressbtn;
	private String[] mission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currentmissiontab);
		missioneventid = (TextView)findViewById(R.id.eventID2);
		missionpriority = (TextView)findViewById(R.id.eventPriority2);
		missionheader = (TextView)findViewById(R.id.missionheader2);
		missionaddress = (TextView)findViewById(R.id.missionaddress2);
		missioninjuries = (TextView)findViewById(R.id.missioninjuries2);
		missiontypeofaccident = (TextView)findViewById(R.id.typeOfAccident2);
		missiondescription = (TextView)findViewById(R.id.missiondescription2);

		gotoaddressbtn = (Button)findViewById(R.id.gotoaddressbtn);
		gotoaddressbtn.setOnClickListener(this);
		changedescbtn = (Button)findViewById(R.id.changemissionbtn);
		changedescbtn.setOnClickListener(this);

		mc = new MissionController(MissionView.this);
	}

	public void update(Observable observable, Object data) {
		if(data instanceof String[]){
			mission = (String[]) data;
			updateMissionView(mission);
		}
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

	public void onClick(View v) {
		if(v == gotoaddressbtn){
			GeoPoint gp = mc.getCurrentMissionAddress();   
			if(gp!=null){
				MainView.tabHost.setCurrentTab(0);
				MapGUI.controller.animateTo(gp);
			}else{
				Toast.makeText(getBaseContext(), "Du har inget uppdrag.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	public void onBackPressed(){
//		AlertDialog logout = new AlertDialog.Builder(this).create();
//		logout.setMessage("Är du säker på att du vill avsluta?");
//		logout.setButton("Ja", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which){
//				finish();
//			}
//		});
//		logout.setButton2("Nej", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();					
//			}
//		});	
//		logout.show();
	}
}
