package tddd36.grupp3.views;

import java.io.IOException;
import java.net.UnknownHostException;

import tddd36.grupp3.ClientMediator;
import tddd36.grupp3.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private TextView display;
	private EditText user;
	private EditText pass;
	private String serverOutput;
	private Button login;
	private ClientMediator cm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		pass = (EditText) findViewById(R.id.editText2);
		user = (EditText) findViewById(R.id.editText1);
		login = (Button) findViewById(R.id.button1);
		display = (TextView)findViewById(R.id.textView3);

		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				try {
					login();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		});
	}

	public void login() throws UnknownHostException, IOException {
		cm = new ClientMediator();
		cm.connect(""+user.getText(), ""+pass.getText());

			if(cm.getAuth()) {
				AlertDialog login = new AlertDialog.Builder(LoginActivity.this).create();
				login.setMessage("Felaktigt användarnamn eller lösenord");
				login.setButton("OK", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) { 	
					}
				});
				pass.setText("");
				login.show();
			}
			else{
				//If authenticated user travels to choosUnit
				startActivity(new Intent(getBaseContext(),tddd36.grupp3.views.ChooseUnitActivity.class));
			}
		
	}


	@Override
	protected void onPause() {
		//finish login activity when traveling to mainmenu
		super.onPause();
		finish();
	}
}
