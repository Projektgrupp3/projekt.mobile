package tddd36.grupp3.views;

import tddd36.grupp3.R;
import android.app.Activity;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class IncomingCall extends Activity implements OnClickListener{
	Intent intent = null;
	SipAudioCall call = null;
	private SipSession session = null;
	private TextView infobar;
	private Button answer;
	private Button decline;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receiving);
		Bundle extras = getIntent().getExtras();
		intent = (Intent)extras.get("intent"); 
		infobar = (TextView) findViewById(R.id.textView1);
		answer = (Button) findViewById(R.id.button1);
		decline = (Button) findViewById(R.id.button2);
		answer.setOnClickListener(this);
		decline.setOnClickListener(this); 

		try {
			SipAudioCall.Listener listener = new SipAudioCall.Listener() {
				@Override 
				public void onRinging(SipAudioCall call, SipProfile caller) {
					super.onRinging(call, caller);
					try {
						call.answerCall(30);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override 
				public void onCallEnded(SipAudioCall call) {
					super.onCallEnded(call);
					session.endCall();
					finish();
				}

			};

			try{
				session=MainView.manager.getSessionFor(intent);
				call=MainView.manager.takeAudioCall(intent, listener);
				String temp = call.getPeerProfile().getUserName();
				update(temp+" ringer...");		
			}catch (SipException e) {
				e.printStackTrace();
			}

		}catch (Exception e) {
			if (call != null) {
				call.close();
			}      
		}
	}

	public void onPause(){
		super.onPause();
		if(call !=null){
			call.close();
			try {
				call.endCall();
			} catch (SipException e) {
				e.printStackTrace();
			}
		}
		if(session!=null){
			session.endCall();
		}
	}

	public void onClick(View v) {
		if(v==answer){
			try {
				call.answerCall(30);
				update("Samtal med "+call.getPeerProfile().getUserName());
				answer.setVisibility(View.INVISIBLE);
				answer.setClickable(false);
				decline.setText("AVSLUTA SAMTAL");
			} catch (SipException e) {
				e.printStackTrace();
			}
			call.startAudio();
		}
		else if(v==decline){
			finish();
		}
	}

	public void update(final String info){
		runOnUiThread(new Runnable(){
			public void run() {
				infobar.setText(info);
			}
		});
	}

}