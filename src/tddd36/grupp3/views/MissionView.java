package tddd36.grupp3.views;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.MissionController;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MissionView extends Activity implements Observer{

	MissionController mc;
	private TextView missionheader, missiondescription, missionaddress, 
	missiontime, missioninjuries;
	private Button changedesc, gotoadress;
	private String[] mission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.missiontablayout);
		missionheader = (TextView)findViewById(R.id.missionheader2);
		missiondescription = (TextView)findViewById(R.id.missiondescription2);
		missionaddress = (TextView)findViewById(R.id.missionaddress2);
		missiontime = (TextView)findViewById(R.id.missiontime2);
		missioninjuries = (TextView)findViewById(R.id.missioninjuries2);
		
		changedesc = (Button)findViewById(R.id.gotoaddress);
		changedesc.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
			}
		}); 
		mc = new MissionController(MissionView.this);
	}

	public void update(Observable observable, Object data) {
		if(data instanceof String[]){
			mission = (String[]) data;
			updateView(mission);
		}
	}
	public void updateView(String[] missiontext){
		missionheader.setText(missiontext[0]);
		missiondescription.setText(missiontext[1]);
		missionaddress.setText(missiontext[2]);
		missiontime.setText(missiontext[3]);
		missioninjuries.setText(missiontext[4]);
	}

}
