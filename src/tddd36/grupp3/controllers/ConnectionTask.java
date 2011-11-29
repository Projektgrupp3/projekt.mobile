package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

import tddd36.grupp3.R;
import tddd36.grupp3.models.LoginModel;
import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.ObjectType;
import tddd36.grupp3.views.MainView;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionTabView;
import tddd36.grupp3.views.SIPView;
import android.os.AsyncTask;
import android.util.Log;

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
				System.out.println("Förfan "+s);
				String [] list = s.split("/");
				for(int i = 0; i<list.length; i++){
					String[] separated = list[i].split(",");
					Contact c = new Contact(separated[0],separated[1]);
					MainView.db.addRow(c);

				}				
			}
			if(messageFromServer.has("MAP_OBJECTS")){
				Log.d("Här vare et event","HAHHA");
				Event incomingEvent = new Event((gp = new GeoPoint(messageFromServer.getInt("tempCoordX"),
						messageFromServer.getInt("tempCoordY"))),
						messageFromServer.getString("header"),
						messageFromServer.get("description").toString());
				MapGUI.mapcontroller.addMapObject(incomingEvent);
				MainView.db.addRow(incomingEvent);	
			}

			else if(messageFromServer.has("event")){
				try {
					Event incomingEvent = new Event(messageFromServer);
					MapGUI.mapcontroller.addMapObject(incomingEvent);
					MissionTabView.mc.setCurrentMission(incomingEvent);
					MainView.db.addRow(incomingEvent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("Avslutar","Task färdig");
	}
}
