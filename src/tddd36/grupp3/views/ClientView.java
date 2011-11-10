package tddd36.grupp3.views;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.ClientController;
import tddd36.grupp3.models.ClientModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClientView extends Activity implements Observer{

	private TextView display;
	private EditText user;
	private EditText pass;
	private Button login;
	ClientController cc;

	public void update(Observable observable, Object data) {
		
		if(data instanceof String){
			Toast.makeText(getBaseContext(), (String)data, Toast.LENGTH_SHORT).show();
			cc.getConnectionController().send("What up?");

		}
		else {
			if(cc.isAuthenticated()) {
				AlertDialog login = new AlertDialog.Builder(ClientView.this).create();
				login.setMessage("Felaktigt användarnamn eller lösenord");
				login.setButton("OK", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) { 	
					}
				});
				pass.setText("");
				login.show();
			}
			else {

				Intent nextIntent = new Intent(getBaseContext(), tddd36.grupp3.views.UnitView.class);
				startActivity(nextIntent);
				//Toast.makeText(getBaseContext(), "Inloggad", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		pass = (EditText) findViewById(R.id.editText2);
		user = (EditText) findViewById(R.id.editText1);
		login = (Button) findViewById(R.id.button1);
		display = (TextView)findViewById(R.id.textView3);

		cc = new ClientController(this);

		user.setText("test");
		pass.setText("password");

		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				cc.setUserName(""+user.getText());
				cc.setPassword(""+pass.getText());
				cc.connectToServer();	
			};
		});
	}

	@Override
	protected void onPause() {
		//finish login activity when traveling to mainmenu
		super.onPause();
		finish();
	}

}
