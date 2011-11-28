package tddd36.grupp3.models;

import java.io.IOException;
import java.util.Observable;

import org.json.JSONObject;

import tddd36.grupp3.controllers.ConnectionController;
import tddd36.grupp3.controllers.LoginController;
import tddd36.grupp3.views.LoginView;

public class LoginModel extends Observable {

	private String userName;
	private String password;

	private static boolean authenticated = false;

	private ConnectionController connectionController;

	private LoginView loginview;
	private JSONObject messageFromServer;

	public LoginModel(LoginView lv, LoginController lc){
		this.loginview = lv;
		try {
			connectionController = new ConnectionController(this);
			connectionController.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		addObserver(lv);
		addObserver(lc);
	}

	public void executeChange(){
		setChanged();
	}
	public void notify(Object o){
		notifyObservers(o);
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
		authenticated = auth;
	}
	public ConnectionController getConnectionController() {
		return connectionController;
	}
}
