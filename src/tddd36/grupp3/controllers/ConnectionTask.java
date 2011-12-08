package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.R;
import tddd36.grupp3.Sender;
import tddd36.grupp3.misc.SplashEvent;
import tddd36.grupp3.models.LoginModel;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.FloodEvent;
import tddd36.grupp3.resources.OtherEvent;
import tddd36.grupp3.resources.RoadBlockEvent;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MissionGroupActivity;
import tddd36.grupp3.views.SIPView;
import tddd36.grupp3.views.TabGroupActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class ConnectionTask extends AsyncTask<Void, Integer, ArrayList<String>> {

	private LoginModel loginModel;
	private SSLSocket socket = null;
	private BufferedReader in;
	private ConnectionController cc;
	private JSONObject messageFromServer;
	private boolean authenticated;
	private SIPView sip;
	ArrayList<String> buffer = new ArrayList<String>();

	private static GeoPoint gp;

	public ConnectionTask(LoginModel lm) {
		this.loginModel = lm;
	}
	public ConnectionTask(SSLSocket socket, ConnectionController cc, LoginModel lm) {
		this.socket = socket;
		this.cc = cc;
		this.loginModel = lm;
		Log.d("Connection Task", "Connection task skapad");
	}

	public void getInput() throws IOException {
		String input;
		try{
			while(true){
				if((input = in.readLine()) != ""){
					buffer.add(input);
					while(in.ready()){
						input = in.readLine();
						buffer.add(input);
					}
					Log.d("Meddelande", "Servern:" +input);
					break;
				}
			}
		} catch(NullPointerException e){
		}
	}

	protected ArrayList<String> doInBackground(Void... params) {
		String input = null;

		try 
		{
			in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			getInput();

			in.close();
			socket.close();
			Log.d("Avslutar", "Socket stängd");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	protected void onPostExecute(ArrayList<String> buffer) {
		super.onPostExecute(buffer);
		Log.d("Antal objekt i buffern",""+buffer.size());
		for(String result : buffer){
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
						if(!MainView.db.checkRow(separated[1])){
							Log.d("ConnectionTask:","L�gger till kontakt:"+separated[0]+" "+separated[1]);
							MainView.db.addRow(c);
						}
					}				
				}
				if(messageFromServer.has(Sender.UPDATE_MAP_OBJECT)){
					Event incomingEvent;
					if(messageFromServer.getString("header").equals("Översvämning")){
						incomingEvent = new FloodEvent((gp = new GeoPoint(messageFromServer.getInt("tempCoordX"),
								messageFromServer.getInt("tempCoordY"))),
								messageFromServer.getString("header"),
								messageFromServer.get("description").toString(), messageFromServer.getString("eventID").toString(), R.drawable.flood_icon);
					}
					else if(messageFromServer.getString("header").equals("Hinder på vägen")){
						incomingEvent = new RoadBlockEvent((gp = new GeoPoint(messageFromServer.getInt("tempCoordX"),
								messageFromServer.getInt("tempCoordY"))),
								messageFromServer.getString("header"),
								messageFromServer.get("description").toString(), messageFromServer.getString("eventID").toString(), R.drawable.road_closed_icon);
					}
					else{
						incomingEvent = new OtherEvent((gp = new GeoPoint(messageFromServer.getInt("tempCoordX"),
								messageFromServer.getInt("tempCoordY"))),
								messageFromServer.getString("header"),
								messageFromServer.get("description").toString(), messageFromServer.getString("eventID").toString(), R.drawable.green_flag_icon);
					}
					MainView.mapController.addMapObject(incomingEvent);
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

					System.out.println("Tar emot event från server.");
					if(messageFromServer.getBoolean("accepted")){
						Log.d("message from server",messageFromServer.toString());
						SplashEvent.cd.stopRunning();
						SplashEvent.mp.stop();
						SplashEvent.vr.cancel();
						Event ev = new Event(messageFromServer, R.drawable.red_flag_icon);
						MainView.mapController.addMapObject(ev);
						MainView.missionController.setActiveMission(ev);
						MainView.db.addRow(ev);
						System.out.println("Event har eventID: "+ev.getID());
						SplashEvent.parentActivity.onBackPressed();
						Sender.send(Sender.ACK_RECIEVED_EVENT+":"+ev.getID());
					}
					else{
						MainView.tabHost.setCurrentTab(1);
						Intent splashIntent = new Intent(MainView.context, SplashEvent.class);
						TabGroupActivity parentActivity = (TabGroupActivity) MissionGroupActivity.getTabParent();
						splashIntent.putExtra("json", messageFromServer.toString());
						parentActivity.startChildActivity("IncomingEvent", splashIntent);
					}
				}
				buffer.remove(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Log.d("Avslutar","Task färdig");
	}
}
