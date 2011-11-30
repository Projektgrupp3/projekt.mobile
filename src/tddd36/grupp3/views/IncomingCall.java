package tddd36.grupp3.views;

import tddd36.grupp3.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IncomingCall extends Activity implements OnClickListener{
	Intent intent = null;
	SipAudioCall call = null;
	private SipSession session = null;
	private TextView infobar;
	private Button answer;
	private Button decline;
	private MediaPlayer ringTone;
	private Vibrator vr;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incomming_call);
		Bundle extras = getIntent().getExtras();
		intent = (Intent)extras.get("intent"); 
		infobar = (TextView) findViewById(R.id.tvSetCaller);
		answer = (Button) findViewById(R.id.bAccept);
		decline = (Button) findViewById(R.id.bDecline);
		answer.setOnClickListener(this);
		decline.setOnClickListener(this); 

		vr = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		final long[] pattern = {0,900,600};
		vr.vibrate(pattern,0);
		ringTone = MediaPlayer.create(IncomingCall.this, R.raw.warning);
		ringTone.start();	
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
		vr.cancel();
		ringTone.release();
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

		vr.cancel();
		ringTone.release();
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
