package tddd36.grupp3.misc;

import java.lang.ref.WeakReference;
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
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class SplashEvent extends Activity implements OnClickListener, Observer {
	private WeakReference<MainView> ctx;

	private CountDown cd;
	private String JSONString;
	private String countDownValue;

	private Event ev;

	private Button acceptmission, rejectmission;
	private TextView timelefttv;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incomingevent);		

		Gson gson = new Gson();

		JSONString = (String) getIntent().getExtras().get("json");

		try {
			JSONObject json = new JSONObject(JSONString);
			ev = new Event(json);
			Toast.makeText(getBaseContext(), "julkorv", Toast.LENGTH_SHORT).show();
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


	public void onClick(DialogInterface dialog, int which) {

	}
	public void update(Observable observable, Object data) {
		countDownValue = (String) data;
		runOnUiThread(new Runnable(){
			public void run() {
				timelefttv.setText(countDownValue);
			}			
		});
	}


	public void onClick(View v) {
		TabGroupActivity parentActivity = (TabGroupActivity) MissionGroupActivity.getTabParent();
		if(ev == null){
			Toast.makeText(getBaseContext(), "Event �r tomt", Toast.LENGTH_SHORT).show();
		}else {
			MapGUI.mapcontroller.addMapObject(ev);
			MissionTabView.mc.setCurrentMission(ev);
			MainView.db.addRow(ev);
			try {
				Sender.send("ack: ACCEPTERAT:"+ev.getID());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		parentActivity.onBackPressed();
	}		
}