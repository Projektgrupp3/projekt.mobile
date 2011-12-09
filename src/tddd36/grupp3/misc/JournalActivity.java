package tddd36.grupp3.misc;


import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.views.MainView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JournalActivity extends Activity implements OnClickListener, Observer {

	TextView name,socialnumber,address,bloodtype,allergies,warning;
	EditText editsocialnumner;
	Button patientjournalbtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientjournallayout);

		MainView.missionController.getMissionModel().addObserver(this);

		name = (TextView)findViewById(R.id.patientName2);
		socialnumber = (TextView)findViewById(R.id.patientSocialNumber2);
		address = (TextView)findViewById(R.id.patientAddress2);
		bloodtype = (TextView)findViewById(R.id.patientBloodType2);
		allergies = (TextView)findViewById(R.id.patientAllergies2);
		warning = (TextView)findViewById(R.id.patientWarning2);
		editsocialnumner = (EditText)findViewById(R.id.patientSocicalNumberEditText1);
		patientjournalbtn = (Button)findViewById(R.id.patientGetJournalbtn);
		patientjournalbtn.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(editsocialnumner.getText().length() == 10){
			Sender.send("REQ_JOURNAL"+":"+editsocialnumner.getText());
		}
		else
			Toast.makeText(this, "Fyll i personnumret korrekt", Toast.LENGTH_LONG).show();
	}

	public void update(Observable observable, Object data) {
		if(data instanceof Journal){
			name.setText(((Journal) data).getName());
			socialnumber.setText(((Journal) data).getSocialnumber());
			address.setText(((Journal) data).getAddress());
			bloodtype.setText(((Journal) data).getBloodtype());
			allergies.setText(((Journal) data).getAllergies());
			warning.setText(((Journal) data).getWarning());
		}
	}
}
