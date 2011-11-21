package tddd36.grupp3.models;

import java.io.IOException;
import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.controllers.LoginController;
import tddd36.grupp3.controllers.ConnectionController;

import tddd36.grupp3.views.LoginView;

public class LoginModel extends Observable {

	private String userName;
	private String password;
	private static boolean authenticated = false;
	private ConnectionController connectionController;

	private LoginView cv;
	private JSONObject messageFromServer;

	public LoginModel(LoginView cv, LoginController cc){
		this.cv = cv;
		try {
			connectionController = new ConnectionController(this);
			connectionController.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addObserver(cv);
		addObserver(cc);
		addObserver(connectionController);
	}

	public void evaluateMessage(String mess) throws JSONException{
		String message;
		messageFromServer = new JSONObject(mess);
		setChanged();
		
		if(messageFromServer.has("msg")){
			message = messageFromServer.getString("msg");
			if(message.equals("authenticated")){
				authenticated = true;
				notifyObservers(authenticated);
			}
			if(message.equals("authfailed")){
				authenticated = false;
				notifyObservers(authenticated);
			}
			else
				notifyObservers(message);
		}
		if(messageFromServer.has("larm")){
			try {
				messageFromServer = new JSONObject(mess);
				notifyObservers(messageFromServer.getString("adress"));
				notifyObservers(messageFromServer.getString("numberOfInjured"));
				notifyObservers(messageFromServer.getString("alarmID"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void executeChange(){
		setChanged();
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String pass) {
		this.password = pass;
	}
	public boolean isAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(boolean auth) {
		this.authenticated = auth;
	}
	public ConnectionController getConnectionController() {
		return connectionController;
	}
}
