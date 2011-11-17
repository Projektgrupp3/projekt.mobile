package tddd36.grupp3.models;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.controllers.LoginController;
import tddd36.grupp3.controllers.ConnectionController;
import tddd36.grupp3.controllers.ConnectionTask;

import tddd36.grupp3.views.LoginView;

public class LoginModel extends Observable {

	private String userName;
	private String password;
	private static boolean authenticated = false;
	private ConnectionController connectionController;
	private int connectAttempts = 0;

	private String messageFromServer;
	private LoginView cv;

	public LoginModel(LoginView cv, LoginController cc){
		this.cv = cv;
		try {
			connectionController = new ConnectionController(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addObserver(cv);
		addObserver(cc);
		addObserver(connectionController);
	}

	public void connectToServer() {

		String[] usernamePassword = {userName,password};
		if(connectAttempts == 0){
			connectAttempts = 1;
			connectionController.execute(usernamePassword);
		} 
		//else
			//connectionController.setLogin(true);
	}

	public void evaluateMessage(String message){
		setChanged();
		if(message.equals("authenticated")){
			authenticated = true;
			notifyObservers(authenticated);
		}
		if(message.equals("authfailed")){
			authenticated = false;
			notifyObservers(authenticated);
		}
		//		else{
		//			//if(message.charAt(2) == 'a'){
		//			try {
		//				JSONObject alarm = new JSONObject(message);
		//
		//				String[] nyttAlarm = new String[3];
		//				nyttAlarm[0] = alarm.getString("adress");
		//				nyttAlarm[1] = alarm.getString("numberOfInjured");
		//				nyttAlarm[2] = alarm.getString("alarmID");
		//
		//			} catch (JSONException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
		else
			notifyObservers(message);
	}

	public void newMessage(String str){
		this.messageFromServer = str;
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
