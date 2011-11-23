package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.models.LoginModel;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MapGUI;
import tddd36.grupp3.views.MissionView;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionTask extends AsyncTask<Void, Integer, String> {

	public static final int LISTEN_PORT = 4445;
	private LoginModel loginModel;
	private Socket socket = null;
	private BufferedReader in;
	private String msg;
	private ConnectionController cc;
	private JSONObject messageFromServer;
	private boolean authenticated;

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

			if(messageFromServer.has("msg")){
				message = messageFromServer.getString("msg");
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
				else{
					loginModel.executeChange();
					loginModel.notify(message);
				}
			}
			else if(messageFromServer.has("event")){
				try {
					Event incomingEvent = new Event(messageFromServer);
					MapGUI.mapcontroller.addMapObject(incomingEvent);
					MissionView.mc.setCurrentMission(incomingEvent);
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
