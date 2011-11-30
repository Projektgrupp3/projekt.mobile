package tddd36.grupp3.views;

import org.json.JSONArray;

import tddd36.grupp3.R;
import android.app.Activity;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MakeCall extends Activity implements OnClickListener {
	public String sipAddress = null;
	public SipAudioCall call = null;
	public JSONArray json = null;
	public TextView callingAddress;
	public Button cancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calling);
		Bundle extras = getIntent().getExtras();
		cancel =(Button) findViewById(R.id.button1);
		callingAddress = (TextView) findViewById(R.id.textView1);
		cancel.setOnClickListener(this);
		String[] temp = null;		
		if (extras != null) {
			temp = extras.getStringArray("info");
			update("Ringer "+temp[0]);
			initiateCall(temp[1]);
		}
	}

	public void initiateCall(final String SIPAddress) {
		try {

			SipAudioCall.Listener audioListener = new SipAudioCall.Listener(){
				@Override
				public void onCallEstablished(SipAudioCall call) {
					update("Samtal med "+call.getPeerProfile().getUserName());
					call.startAudio();
					call.setSpeakerMode(true);
					if (call.isMuted()) {
						call.toggleMute();
					}
				}

				@Override
				public void onCallEnded(SipAudioCall call) {
					Log.d("End call","End call");
				//Looper.prepare();
					endCall();
				}

				@Override
				public void onError(SipAudioCall call, int errorCode,
						String errorMessage) {
					endCall();
				}
				@Override
				public void onCallBusy(SipAudioCall call) {
					endCall();
				}

			};

			//			SipSession.Listener sessionListener = new SipSession.Listener() {
			//				@Override
			//				public void onCallBusy(SipSession session) {
			//					endCall();
			//				}
			//
			//				@Override
			//				public void onCallEnded(SipSession session) {
			//					session.endCall();
			//					endCall();
			//				}
			//				@Override
			//				public void onError(SipSession session, int errorCode,
			//						String errorMessage) {
			//				endCall();
			//				}
			//			};

			call = MainView.manager.makeAudioCall(MainView.me.getUriString(), SIPAddress, audioListener, 30);
			//MainView.manager.createSipSession(MainView.me, sessionListener);

		}
		catch (Exception e) {
			if (MainView.me != null) {
				try {
					MainView.manager.close(MainView.me.getUriString());
				} catch (Exception ee) {ee.printStackTrace();}
			}
			if (call != null) {
				call.close();
			}
		}
	}
	public void update(final String s){
		runOnUiThread(new Runnable(){
			public void run() {
				callingAddress.setText(s);
			}

		});
	}

	public void onPause(){
		super.onPause();
		//		try {
		//			if (call != null) {
		//				call.close();
		//				call.endCall();
		//			}
		//		} catch (SipException e) {
		//			e.printStackTrace();
		//		}
		//finish();
	}

	public void onClick(View v) {
			endCall();
	}

	public void endCall(){
		try {
			if (call != null) {
				call.endCall();
				call.close();
			}
		} catch (SipException e) {
			e.printStackTrace();
		}
		runOnUiThread(new Runnable(){
			public void run() {
				TabGroupActivity parentActivity = (TabGroupActivity)getParent();
				parentActivity.onBackPressed();
			}
		});
	}
}
