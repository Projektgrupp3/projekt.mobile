package tddd36.grupp3.views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.ConnectionController;
import tddd36.grupp3.controllers.LoginController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginView extends Activity implements Observer,  OnItemSelectedListener{
	//Loginskärms variabler
	private TextView display;
	private EditText user;
	private EditText pass;
	private Button login;
	LoginController cc;
	private boolean authenticated;
	public int counter = 0;

	//Unit variabler
	private Spinner spinner;
	private Button bContinue;
	private String[] unitNames = {"1","2","3"};
	private ArrayList<String> allUnits = new ArrayList<String>();

	public void update(Observable observable, Object data) {

		if(data instanceof String){
			Toast.makeText(getBaseContext(), (String)data, Toast.LENGTH_SHORT).show();
		}
		else if(data instanceof Boolean){
			authenticated = (Boolean) data;
			
			if(!authenticated) {
				Toast.makeText(getBaseContext(), "false", Toast.LENGTH_SHORT).show();
				final AlertDialog login = new AlertDialog.Builder(LoginView.this).create();
				login.setMessage("Felaktigt användarnamn eller lösenord");
				login.setButton("OK", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) { 
					}
				});
				pass.setText("");
				login.show();
			}
			else {
				Toast.makeText(getBaseContext(), "true", Toast.LENGTH_SHORT).show();
				chooseUnit();
			}
		}
	}

	public void chooseUnit(){
		setContentView(R.layout.unit);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, unitNames);
		spinner = (Spinner) findViewById(R.id.spinner1);
		bContinue = (Button) findViewById(R.id.bContinue);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		bContinue.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(),tddd36.grupp3.views.MainView.class));
			};
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		pass = (EditText) findViewById(R.id.editText2);
		user = (EditText) findViewById(R.id.editText1);
		login = (Button) findViewById(R.id.button1);
		display = (TextView)findViewById(R.id.textView3);

		user.setText("test");
		pass.setText("password");

		cc = new LoginController(this);
		
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				try {
				cc.getConnectionController().send(""+user.getText(), ""+pass.getText(), null);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			};
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
//	
//	@Override
//	protected void onPause() {
//		super.onPause();
//		//finish();
//	}
//	@Override
//	protected void onStop() {
//		super.onPause();
//		finish();
//	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		int position = spinner.getSelectedItemPosition();
		switch(position){
		case 0:

			break;
		case 1:

			break;
		case 2:

			break;
		}
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

}
