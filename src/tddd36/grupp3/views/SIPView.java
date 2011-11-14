package tddd36.grupp3.views;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.controllers.SIPController;

import tddd36.grupp3.models.SIPSettingsModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SIPView extends Activity implements View.OnTouchListener, Observer{
	
	SIPController sipcontroller;
	SipAudioCall call;
	
	private static final int CALL_ADDRESS = 1;
	private static final int SET_AUTH_INFO = 2;
	private static final int UPDATE_SETTINGS_DIALOG = 3;
	private static final int HANG_UP = 4;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.walkietalkie);

		ToggleButton pushToTalkButton = (ToggleButton) findViewById(R.id.pushToTalk);
		pushToTalkButton.setOnTouchListener(this);
		
		sipcontroller = new SIPController(this);
		call = sipcontroller.getSIPModel().call;
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	/**
	 * Updates whether or not the user's voice is muted, depending on whether the button is pressed.
	 * @param v The View where the touch event is being fired.
	 * @param event The motion to act on.
	 * @return boolean Returns false to indicate that the parent view should handle the touch event
	 * as it normally would.
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (call == null) {
			return false;
		} else if (event.getAction() == MotionEvent.ACTION_DOWN && call != null && call.isMuted()) {
			call.toggleMute();
		} else if (event.getAction() == MotionEvent.ACTION_UP && !call.isMuted()) {
			call.toggleMute();
		}
		return false;
	}
	
	public SIPController getController(){
		return sipcontroller;
	}

	public void update(Observable observable, Object data) {
		if(data instanceof SipAudioCall){
			String useName = ((SipAudioCall) data).getPeerProfile().getDisplayName();
			if(useName == null) {
				useName = ((SipAudioCall) data).getPeerProfile().getUserName();
			}
			updateStatus(useName + "@" + ((SipAudioCall) data).getPeerProfile().getSipDomain());
		}
		if(data instanceof String){
			updateStatus(data);
		}		
	}
	
	public void updateStatus(Object data){
		TextView labelView = (TextView) findViewById(R.id.sipLabel);
		labelView.setText((CharSequence) data);
	}
	@Override
	public void onStart() {
		super.onStart();
		// When we get back from the preference setting Activity, assume
		// settings have changed, and re-login with new auth info.
		sipcontroller.getSIPModel().initializeManager();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        sipcontroller.getSIPModel();
		menu.add(0, CALL_ADDRESS, 0, "Ring samtal");
        menu.add(0, SET_AUTH_INFO, 0, "Ändra din SIP-information");
        menu.add(0, HANG_UP, 0, "Avsluta nuvarande samtal");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CALL_ADDRESS:
                showDialog(CALL_ADDRESS);
                break;
            case SET_AUTH_INFO:
                updatePreferences();
                break;
            case HANG_UP:
                if(call != null) {
                    try {
                      call.endCall();
                    } catch (SipException se) {
                        Log.d("WalkieTalkieActivity/onOptionsItemSelected",
                                "Fel vid nedkopplat samtal", se);
                    }
                    call.close();
                }
                break;
        }
        return true;
    }
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CALL_ADDRESS:

			LayoutInflater factory = LayoutInflater.from(this);
			final View textBoxView = factory.inflate(R.layout.call_address_dialog, null);
			return new AlertDialog.Builder(this)
			.setTitle("Ring")
			.setView(textBoxView)
			.setPositiveButton(
					android.R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							EditText textField = (EditText)
							(textBoxView.findViewById(R.id.calladdress_edit));
							sipcontroller.getSIPModel().sipAddress = textField.getText().toString();
							sipcontroller.getSIPModel().initiateCall();

						}
					})
					.setNegativeButton(
							android.R.string.cancel, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									// Noop.
								}
							})
							.create();

		case UPDATE_SETTINGS_DIALOG:
			return new AlertDialog.Builder(this)
			.setMessage("Var god uppdatera ditt SIP-kontos inställningar.")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					updatePreferences();
				}
			})
			.setNegativeButton(
					android.R.string.cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// No-op
						}
					})
					.create();
		}
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (sipcontroller.getSIPModel().call != null) {
			sipcontroller.getSIPModel().call.close();
		}

		sipcontroller.getSIPModel().closeLocalProfile();

		if (sipcontroller.getSIPModel().callReceiver != null) {
			this.unregisterReceiver(sipcontroller.getSIPModel().callReceiver);
		}
	}

	public void updatePreferences() {
		Intent settingsActivity = new Intent(getBaseContext(),
				SIPSettingsView.class);
		startActivity(settingsActivity);

	}
}
