package tddd36.grupp3.models;

import java.text.ParseException;
import java.util.Observable;

import tddd36.grupp3.IncomingCallReceiver;
import tddd36.grupp3.controllers.SIPController;
import tddd36.grupp3.views.SIPView;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class SIPModel extends Observable{
	
	SIPView sipview;
	
	public String sipAddress = null;

	public SipManager manager = null;
	public SipProfile me = null;
	public SipAudioCall call = null;
	public IncomingCallReceiver callReceiver;
	
	private static final int CALL_ADDRESS = 1;
	private static final int SET_AUTH_INFO = 2;
	private static final int UPDATE_SETTINGS_DIALOG = 3;
	private static final int HANG_UP = 4;

	public SIPModel(SIPView sipview, SIPController sipcontroller) {
		this.sipview = sipview;
		callReceiver = new IncomingCallReceiver();
		
		// Set up the intent filter.  This will be used to fire an
		// IncomingCallReceiver when someone calls the SIP address used by this
		// application.
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.SipDemo.INCOMING_CALL");
		sipview.registerReceiver(callReceiver, filter);		
		
		addObserver(sipview);
		addObserver(sipcontroller);
		
		initializeManager();		
	}
//	public static int getCallAddress() {
//		return CALL_ADDRESS;
//	}
//	public static int getSetAuthInfo() {
//		return SET_AUTH_INFO;
//	}
//	public static int getUpdateSettingsDialog() {
//		return UPDATE_SETTINGS_DIALOG;
//	}
//	public static int getHangUp() {
//		return HANG_UP;
//	}
	public void initializeManager() {
		if(manager == null) {
			manager = SipManager.newInstance(sipview.getBaseContext());
		}
		initializeLocalProfile();
	}
	/**
	 * Logs you into your SIP provider, registering this device as the location to
	 * send SIP calls to for your SIP address.
	 */
	public void initializeLocalProfile() {
		if (manager == null) {
			return;
		}

		if (me != null) {
			closeLocalProfile();
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(sipview.getBaseContext());
		String username = prefs.getString("namePref", "");
		String domain = prefs.getString("domainPref", "");
		String password = prefs.getString("passPref", "");

		if (username.length() == 0 || domain.length() == 0 || password.length() == 0) {
			notifyObservers(UPDATE_SETTINGS_DIALOG);
			return;
		}

		try {
			SipProfile.Builder builder = new SipProfile.Builder(username, domain);
			builder.setPassword(password);
			me = builder.build();

			Intent i = new Intent();
			i.setAction("android.SipDemo.INCOMING_CALL");
			PendingIntent pi = PendingIntent.getBroadcast(sipview.getBaseContext(), 0, i, Intent.FILL_IN_DATA);
			manager.open(me, pi, null);


			// This listener must be added AFTER manager.open is called,
			// Otherwise the methods aren't guaranteed to fire.

			manager.setRegistrationListener(me.getUriString(), new SipRegistrationListener() {
				public void onRegistering(String localProfileUri) {
					notifyObservers("Registrerar mot SIP-server..");
				}

				public void onRegistrationDone(String localProfileUri, long expiryTime) {
					notifyObservers("Redo.");
				}

				public void onRegistrationFailed(String localProfileUri, int errorCode,
						String errorMessage) {
					notifyObservers("Registrering misslyckades. Var god se över dina inställningar.");
				}
			});
		} catch (ParseException pe) {
			notifyObservers("Fel i uppkoppling.");
		} catch (SipException se) {
			notifyObservers("Fel i uppkoppling.");
		}
	}
	/**
	 * Closes out your local profile, freeing associated objects into memory
	 * and unregistering your device from the server.
	 */
	public void closeLocalProfile() {
		if (manager == null) {
			return;
		}
		try {
			if (me != null) {
				manager.close(me.getUriString());
			}
		} catch (Exception ee) {
			Log.d("WalkieTalkieActivity/onDestroy", "Misslyckades med att stänga den lokala profilen.", ee);
		}
	}
	/**
	 * Make an outgoing call.
	 */
	public void initiateCall() {

		notifyObservers(sipAddress);

		try {
			SipAudioCall.Listener listener = new SipAudioCall.Listener() {
				// Much of the client's interaction with the SIP Stack will
				// happen via listeners.  Even making an outgoing call, don't
				// forget to set up a listener to set things up once the call is established.
				@Override
				public void onCallEstablished(SipAudioCall call) {
					call.startAudio();
					call.setSpeakerMode(true);
					call.toggleMute();
					notifyObservers(call);
				}

				@Override
				public void onCallEnded(SipAudioCall call) {
					notifyObservers("Redo.");
				}
			};

			call = manager.makeAudioCall(me.getUriString(), sipAddress, listener, 30);

		}
		catch (Exception e) {
			Log.i("WalkieTalkieActivity/InitiateCall", "Fel uppstod vid försök att stänga ner manager.", e);
			if (me != null) {
				try {
					manager.close(me.getUriString());
				} catch (Exception ee) {
					Log.i("WalkieTalkieActivity/InitiateCall",
							"Fel uppstod vid försök att stänga ner manager.", ee);
					ee.printStackTrace();
				}
			}
			if (call != null) {
				call.close();
			}
		}
	}
}
