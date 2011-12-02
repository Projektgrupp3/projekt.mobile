package tddd36.grupp3.misc;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionGroupActivity;
import tddd36.grupp3.views.MissionTabView;
import tddd36.grupp3.views.TabGroupActivity;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

public class SplashEvent extends Activity implements OnClickListener, Observer {

	private CountDown cd;
	private String JSONString;
	private String countDownValue;

	private Event ev;

	private Button acceptmission;
	private TextView timelefttv;
	
	private TabGroupActivity parentActivity;	

	private MediaPlayer mp;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incomingevent);		
		
		parentActivity = (TabGroupActivity) MissionGroupActivity.getTabParent();
		
		mp = MediaPlayer.create(SplashEvent.this, R.raw.warning);
		mp.start();
		
		JSONString = (String) getIntent().getExtras().get("json");

		try {
			JSONObject json = new JSONObject(JSONString);
			ev = new Event(json, R.drawable.red_flag_icon);
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
					parentActivity.onBackPressed();
				}
			}			
		});
	}


	public void onClick(View v) {
		cd.stopRunning();
		mp.stop();
		if(ev == null){
			Toast.makeText(getBaseContext(), "Event är tomt", Toast.LENGTH_SHORT).show();
		}else {
			MapGUI.mapcontroller.addMapObject(ev);
			MissionTabView.mc.setCurrentMission(ev);
			MainView.db.addRow(ev);
			Sender.send(Sender.ACK_ACCEPTED_EVENT+":"+ev.getID());
		}
		parentActivity.onBackPressed();
	}		
}
