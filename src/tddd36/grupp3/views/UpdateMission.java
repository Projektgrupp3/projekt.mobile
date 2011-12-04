package tddd36.grupp3.views;

import java.util.ArrayList;

import org.json.JSONException;

import com.google.gson.Gson;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.controllers.MissionController;
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

	private Event currentMission = MissionTabView.mc.getMm().getCurrentEvent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatemission);

		saveUpdates = (Button)findViewById(R.id.bSaveContact);
		
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
		
		//saveUpdates.setOnClickListener(
	}

	public void onClick(View v) {

		if(E1.getText().toString().equals("") ||E2.getText().toString().equals("")||E3.getText().toString().equals("")
				||E4.getText().toString().equals("")||E5.getText().toString().equals("")
				||E6.getText().toString().equals("")||E7.getText().toString().equals("")){

			Toast.makeText(getBaseContext(), "Fyll i fält ", Toast.LENGTH_SHORT).show();

		}else{
			//			Contact newContact = new Contact(edSaveName.getText().toString(), edSaveSip.getText().toString());
			//			Gson gson = new Gson();
			//			gson.toJson(newContact);// spara rapporten i databasen för historiken oc h skicka till servern.
			//			MainView.db.addRow(newContact);
			//			try {
			//				// Sends the new contact to the server.
			//				Sender.sendContact(newContact.getName(), newContact.getSipaddress());
			//			      } catch (JSONException e) {
			//			        // TODO Auto-generated catch block
			//			       e.printStackTrace();
			//			     }
			//			Toast.makeText(getBaseContext(),edSaveName.getText().toString() + " har lagts till!" , Toast.LENGTH_SHORT).show();
			Toast.makeText(getBaseContext(), "OBS! Ej sparat på klient/server", Toast.LENGTH_SHORT).show();
			finish();

		}

	}

}
