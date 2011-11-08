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

		if(ClientModel.isAuthenticated()) {
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
			startActivity(new Intent(getBaseContext(),tddd36.grupp3.ChooseUnitActivity.class));
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
		
		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getBaseContext(), ""+user.getText(), Toast.LENGTH_SHORT).show();
				Toast.makeText(getBaseContext(), ""+pass.getText(), Toast.LENGTH_SHORT).show();
				
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
