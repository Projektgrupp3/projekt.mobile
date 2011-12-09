package tddd36.grupp3.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import com.google.gson.Gson;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.models.MissionModel;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.Event;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateMission extends Activity implements OnClickListener{

	private Button saveUpdates;
	private EditText E1,E2,E3,E4,E5,E6,E7 ;

	private Event currentMission;
	Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatemission);
		currentMission = gson.fromJson(getIntent().getExtras().getString("mission"), Event.class);
		saveUpdates = (Button)findViewById(R.id.bUpdateMission);
		
		E1 = (EditText)findViewById(R.id.edEventID2);
		E2 = (EditText)findViewById(R.id.edMissionheader2);
		E3 = (EditText)findViewById(R.id.edMissiondescription2);
		E4 = (EditText)findViewById(R.id.edMissionaddress2);
		E5 = (EditText)findViewById(R.id.edMissioninjuries2);
		E6 = (EditText)findViewById(R.id.edTypeOfAccident2);
		E7 = (EditText)findViewById(R.id.edEventPriority2);

		E1.setText(currentMission.getID());
		E2.setText(currentMission.getAccidentType());
		E3.setText(currentMission.getDescription());
		E4.setText(currentMission.getAddress());
		E5.setText(""+currentMission.getNumberOfInjured());
		E6.setText(currentMission.getTypeOfInjury());
		E7.setText(currentMission.getPriority());
		
		saveUpdates.setOnClickListener(this);
	}

	public void onClick(View v) {
		Gson gson = new Gson();
		if(E1.getText().toString().equals("") ||E2.getText().toString().equals("")||E3.getText().toString().equals("")
				||E4.getText().toString().equals("")||E5.getText().toString().equals("")
				||E6.getText().toString().equals("")||E7.getText().toString().equals("")){
			Toast.makeText(getBaseContext(), "Fyll i fält ", Toast.LENGTH_SHORT).show();
		}else{
			currentMission.setID(E1.getText().toString());
			currentMission.setHeader(E2.getText().toString());
			currentMission.setAccidentType(E2.getText().toString());
			currentMission.setDescription(E3.getText().toString());
			currentMission.setAdress(E4.getText().toString());
			currentMission.setNumberOfInjured(Integer.parseInt(E5.getText().toString()));
			currentMission.setTypeOfInjury(E6.getText().toString());
			currentMission.setPriority(E7.getText().toString());
			currentMission.setLastChanged(new SimpleDateFormat("yy:MM:dd:HH:mm:ss").format(new Date()));
			MainView.missionController.updateActiveMission(currentMission);
			MainView.mapController.updateMapObject(currentMission);
			Sender.send(gson.toJson(currentMission));
			Toast.makeText(getBaseContext(), "Uppdrag ändrat", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

}
