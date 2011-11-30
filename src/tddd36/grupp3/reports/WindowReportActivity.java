package tddd36.grupp3.reports;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import tddd36.grupp3.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class WindowReportActivity extends Activity implements OnClickListener {
	Button sendVind;
	EditText E1,E2,E3,E4,E5, E6 ;
	RadioButton yes, no ;
	TextView vind;
	String seriousEvent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.windowreport);

		vind =(TextView)findViewById(R.id.tvVind);

		E5 = (EditText)findViewById(R.id.edExactLocation);
		E4 = (EditText)findViewById(R.id.edExtraResourcesVind);
		E3 = (EditText)findViewById(R.id.edNumberOfInjuredVind);
		E2 = (EditText)findViewById(R.id.edThreatsVind);
		E1 = (EditText)findViewById(R.id.edTypeOfInjuryVind);


		yes = (RadioButton)findViewById(R.id.radio0V);
		no = (RadioButton)findViewById(R.id.radio1V);

		sendVind = (Button)findViewById(R.id.bSendVind);
		sendVind.setOnClickListener(this);

	}


	public void checkRadioGroup(){

		if(no.isChecked()){
			seriousEvent = "no";

		}else{
			seriousEvent = "yes";
		}
	}
	public void onClick(View v) {
		if(E1.getText().toString().equals("") ||E2.getText().toString().equals("")
				|| E3.getText().toString().equals("") || E4.getText().toString().equals("") ||
				E5.getText().toString().equals("")){

			Toast.makeText(getBaseContext(), "Fyll i f�lt ", Toast.LENGTH_SHORT).show();

		}else{

			WindowReport wr = new WindowReport(seriousEvent, E1.getText().toString(), 
					E2.getText().toString(), E3.getText().toString(), E4.getText().toString(), E5.getText().toString());

			Gson gson = new Gson();
			gson.toJson(wr);// spara rapporten i databasen f�r historiken oc h skicka till servern.

			Toast.makeText(getBaseContext(), wr.toString(), Toast.LENGTH_LONG);

		}
	}
}



