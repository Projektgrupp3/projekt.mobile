package tddd36.grupp3.misc;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.controllers.MissionController;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionGroupActivity;
import tddd36.grupp3.views.MissionTabView;
import tddd36.grupp3.views.TabGroupActivity;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

public class SplashEvent extends Activity implements OnClickListener, Observer {

	public static CountDown cd;
	private String JSONString;
	private String countDownValue;
	public static Event bufferedEvent;

	private Event ev;

	private Button acceptmission;
	private TextView timelefttv;
	
	public static TabGroupActivity parentActivity;	

	public static MediaPlayer mp;
	public static Vibrator vr;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incomingevent);		
		
		parentActivity = (TabGroupActivity) MissionGroupActivity.me;
		vr = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		final long[] pattern = {0,1500,100};
		vr.vibrate(pattern,0);
		
		mp = MediaPlayer.create(SplashEvent.this, R.raw.alarm);
		mp.start();
		
		JSONString = (String) getIntent().getExtras().get("json");

		try {
			JSONObject json = new JSONObject(JSONString);
			ev = new Event(json, R.drawable.red_flag_icon);
			bufferedEvent = ev;
			Toast.makeText(getBaseContext(), "Inkommande larm!", Toast.LENGTH_SHORT).show();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cd = new CountDown();

		acceptmission = (Button)findViewById(R.id.acceptbtn);
		acceptmission.setOnClickListener(this);
		timelefttv = (TextView)findViewById(R.id.incevent2);
		cd.addObserver(this);
		new Thread(cd).start();
	}	

	public void update(Observable observable, Object data) {
		countDownValue = (String) data;

		runOnUiThread(new Runnable(){
			public void run() {
				timelefttv.setText(countDownValue);
				if(countDownValue.equals("0")){
					Sender.send(Sender.ACK_REJECTED_EVENT+":"+ev.getID());
					cd.stopRunning();
					mp.stop();
					vr.cancel();
					finish();
				}				
			}			
		});
		
	}

	public void onClick(View v) {
		runOnUiThread(new Runnable(){
			public void run() {
				cd.stopRunning();
				mp.stop();
				vr.cancel();
				if(ev == null){
					Toast.makeText(getBaseContext(), "Event är tomt", Toast.LENGTH_SHORT).show();
				}else {
					MainView.mapController.addMapObject(ev);
					MainView.missionController.setActiveMission(ev);
					Sender.send(Sender.ACK_ACCEPTED_EVENT+":"+ev.getID());
				}
				finish();
			}			
		});
		
	}		
}
