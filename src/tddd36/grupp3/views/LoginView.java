package tddd36.grupp3.views;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.controllers.LoginController;
import tddd36.grupp3.misc.NetworkManager;
import tddd36.grupp3.misc.QoSManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginView extends Activity implements Observer,
OnItemSelectedListener {
	// Loginsk�rms variabler
	private TextView display;
	private EditText user;
	private EditText pass;
	private Button login;
	private ProgressDialog loginwait;

	LoginController logincontroller;
	private boolean authenticated;
	public int counter = 0;

	// Unit variabler
	private Spinner spinner;
	private Button bContinue;
	private String[] unitNames = { "1", "2", "3" };
	public static String[] allUnits = { "" };
	private int spinnerPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		NetworkManager.chkStatus(LoginView.this);
		
		pass = (EditText) findViewById(R.id.editText2);
		user = (EditText) findViewById(R.id.editText1);
		login = (Button) findViewById(R.id.button1);
		display = (TextView) findViewById(R.id.textView3);

		user.setText("enhet2");
		pass.setText("password2");
		loginwait = new ProgressDialog(this);
		loginwait.setTitle("Loggar in..");
		loginwait.setCancelable(false);
		
		logincontroller = new LoginController(this);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					loginwait.show();
					Sender.send("" + user.getText(), "" + pass.getText(),
							Sender.REQ_ALL_UNITS);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		});
	}

	public void update(Observable observable, Object data) {

		if (data instanceof String) {
			Toast.makeText(getBaseContext(), (String) data, Toast.LENGTH_SHORT)
			.show();
		}

		if (data instanceof String[]) {
			allUnits = (String[]) data;
			Log.d("Här", "kan man vara");
			try {
				if (authenticated) {
					Log.d("Här", "kan man vara2");
					loginwait.dismiss();
					chooseUnit();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (data instanceof Boolean) {
			authenticated = (Boolean) data;

			if (!authenticated) {
				Toast.makeText(getBaseContext(), "false", Toast.LENGTH_SHORT)
				.show();
				final AlertDialog login = new AlertDialog.Builder(
						LoginView.this).create();
				login.setMessage("Felaktigt användarnamn eller lösenord");
				login.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						loginwait.dismiss();
					}
				});
				pass.setText("");
				login.show();
			} else {
				authenticated = true;
			}
		}
	}

	public void chooseUnit() throws JSONException {
		setContentView(R.layout.unit);
		loginwait.dismiss();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, allUnits);
		spinner = (Spinner) findViewById(R.id.spinner1);
		bContinue = (Button) findViewById(R.id.bContinue);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		bContinue.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Sender.send(Sender.ACK_CHOSEN_UNIT + ":"
						+ allUnits[spinnerPosition]);
				loginwait.show();
				Intent mainIntent = new Intent(getBaseContext(),
						tddd36.grupp3.views.MainView.class);
				mainIntent.putExtra("user", "" + user.getText());
				mainIntent.putExtra("pass", "" + pass.getText());
				startActivity(mainIntent);
				finish();
			};
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		loginwait.dismiss();
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		spinnerPosition = spinner.getSelectedItemPosition();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onBackPressed() {
	}
}
