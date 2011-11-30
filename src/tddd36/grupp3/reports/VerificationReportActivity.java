package tddd36.grupp3.reports;

import tddd36.grupp3.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class VerificationReportActivity extends Activity implements OnClickListener {
	Button sendVer;
	EditText E1,E2,E3,E4,E5,E6 ;
	RadioButton yes, no ;
	TextView ver;
	String seriousEvent;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verificationreport);


		sendVer = (Button)findViewById(R.id.bSendInfo);
		sendVer.setOnClickListener(this);
		ver = (TextView)findViewById(R.id.tvVer);
	//	E6 = (EditText)findViewById(R.id.edAreaSearched);
		E5 = (EditText)findViewById(R.id.edTimeOfDepature);
		E4 = (EditText)findViewById(R.id.edExtraResources);
		E3 = (EditText)findViewById(R.id.edNumberOfInjuries);
		E2 = (EditText)findViewById(R.id.edThreats);
		E1 = (EditText)findViewById(R.id.edTypeOfInjury);
		yes = (RadioButton)findViewById(R.id.radio0);
		no = (RadioButton)findViewById(R.id.radio1);

	}

	public void checkRadioGroup(){

		if(no.isChecked()){
			seriousEvent = "no";

		}else{
			seriousEvent = "yes";
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		//		Toast.makeText(getBaseContext(), "Verifieringsrapport skickad " + E4.getText(), Toast.LENGTH_SHORT).show();
		if(E1.getText().toString().equals("") ||E2.getText().toString().equals("")
				|| E3.getText().toString().equals("") || E4.getText().toString().equals("") ||
				E5.getText().toString().equals("") ||
				E6.getText().toString().equals("") ){

			Toast.makeText(getBaseContext(), "Fyll i f�lt ", Toast.LENGTH_SHORT).show();

		}else{

			VerificationReport vr = new VerificationReport(seriousEvent, E1.getText().toString(), 
					E2.getText().toString(), E3.getText().toString(), E4.getText().toString(), E6.getText().toString() , E5.getText().toString());

			Gson gson = new Gson();
			gson.toJson(vr);// spara rapporten i databasen f�r historiken oc h skicka till servern.
			
			Toast.makeText(getBaseContext(), vr.toString(), Toast.LENGTH_LONG).show();

		}



	}




}
