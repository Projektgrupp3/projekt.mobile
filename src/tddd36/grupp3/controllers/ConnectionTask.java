package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.R;
import tddd36.grupp3.misc.SplashEvent;
import tddd36.grupp3.models.LoginModel;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.OtherEvent;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionGroupActivity;
import tddd36.grupp3.views.SIPView;
import tddd36.grupp3.views.TabGroupActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class ConnectionTask extends AsyncTask<Void, Integer, String> {

	private LoginModel loginModel;
	private Socket socket = null;
	private BufferedReader in;
	private ConnectionController cc;
	private JSONObject messageFromServer;
	private boolean authenticated;
	private SIPView sip;

	private static GeoPoint gp;

	public ConnectionTask(LoginModel lm) {
		this.loginModel = lm;
	}
	public ConnectionTask(Socket socket, ConnectionController cc, LoginModel lm) {
		this.socket = socket;
		this.cc = cc;
		this.loginModel = lm;
		Log.d("Connection Task", "Connection task skapad");
	}

	public String getInput() throws IOException {
		String input;
		try{
			while(true){
				if((input = in.readLine()) != ""){
					Log.d("Meddelande", "Servern:" +input);
					return input;
				}
			}
		} catch(NullPointerException e){
		}
		return null;
	}

	protected String doInBackground(Void... params) {
		String message = null;
		try 
		{
			in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			message = getInput();

			in.close();
			socket.close();
			Log.d("Avslutar", "Socket stängd");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			String message;
			messageFromServer = new JSONObject(result);

			if(messageFromServer.has("auth")){
				message = (String)messageFromServer.get("auth");

				if(message.equals("authenticated")){
					authenticated = true;
					loginModel.executeChange();
					loginModel.notify(authenticated);
				}
				if(message.equals("authfailed")){
					authenticated = false;
					loginModel.executeChange();
					loginModel.notify(authenticated);
				}
			}
			if(messageFromServer.has("contacts")){
				String s = (String)messageFromServer.get("contacts");
				String [] list = s.split("/");
				for(int i = 0; i<list.length; i++){
					String[] separated = list[i].split(",");
					Contact c = new Contact(separated[0],separated[1]);
					MainView.db.addRow(c);
					}				
			}
			if(messageFromServer.has("MAP_OBJECTS")){
				OtherEvent incomingEvent = new OtherEvent((gp = new GeoPoint(messageFromServer.getInt("tempCoordX"),
						messageFromServer.getInt("tempCoordY"))),
						messageFromServer.getString("header"),
						messageFromServer.get("description").toString(), messageFromServer.getString("eventID").toString(), R.drawable.green_flag_icon);
				MapGUI.mapcontroller.addMapObject(incomingEvent);
				MainView.db.addRow(incomingEvent);	
			}
			if(messageFromServer.has("ALL_UNITS")){
				int count = messageFromServer.getInt("ALL_UNITS");
				String[] allUnits = new String[count];
				for(int i = 0; i< allUnits.length; i++){
					allUnits[i] = messageFromServer.getString("unit"+i);
				}
				loginModel.executeChange();
				loginModel.notify(allUnits);
			}

			else if(messageFromServer.has("event")){
				MainView.tabHost.setCurrentTab(1);
				Intent splashIntent = new Intent(MainView.context, SplashEvent.class);
				TabGroupActivity parentActivity = (TabGroupActivity) MissionGroupActivity.getTabParent() ;
				splashIntent.putExtra("json", messageFromServer.toString());
				parentActivity.startChildActivity("IncomingEvent", splashIntent);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("Avslutar","Task färdig");
	}
}
