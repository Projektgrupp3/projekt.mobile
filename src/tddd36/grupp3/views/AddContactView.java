package tddd36.grupp3.views;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.resources.Contact;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactView extends Activity implements OnClickListener{
	
	Button saveContact;
	EditText edSaveName, edSaveSip;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcontact);
		
	saveContact = (Button)findViewById(R.id.bSaveContact);
	saveContact.setOnClickListener(this);

	
	edSaveName = (EditText)findViewById(R.id.edSaveName);
	edSaveSip = (EditText)findViewById(R.id.edSaveSip);


	}

	public void onClick(View v) {

		if(edSaveName.getText().toString().equals("") ||edSaveSip.getText().toString().equals("")){

			Toast.makeText(getBaseContext(), "Fyll i fält ", Toast.LENGTH_SHORT).show();

		}else{

			Contact newContact = new Contact(edSaveName.getText().toString(), edSaveSip.getText().toString());
			Gson gson = new Gson();
			gson.toJson(newContact);// spara rapporten i databasen för historiken oc h skicka till servern.
			MainView.db.addRow(newContact);
			try {
				Sender.sendContact(newContact.getName(), newContact.getSipaddress());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			Toast.makeText(getBaseContext(),edSaveName.getText().toString() + " har lagts till!" , Toast.LENGTH_SHORT).show();
			finish();
			
		}
		
	}

}
