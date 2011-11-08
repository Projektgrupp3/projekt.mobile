package tddd36.grupp3.models;

import java.util.Observable;

import tddd36.grupp3.controllers.ClientController;
import tddd36.grupp3.controllers.ConnectionController;
import tddd36.grupp3.views.ClientView;

public class ClientModel extends Observable {

	private String userName;
	private String password;
	private static boolean authenticated;
	private ConnectionController connectionController;

	public ClientModel(ClientView cv, ClientController cc){
		connectionController = new ConnectionController();
		addObserver(cv);
		addObserver(cc);
		addObserver(connectionController);
	}

	public void connectToServer(){
		//connectionController.run(userName, password); Om man vill köra en vanligt tråd istället.
		String[] unPw = {userName, password};
		connectionController.execute(unPw);
		hasChanged();
		notifyObservers();
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

	public static boolean isAuthenticated() {
		return authenticated;
	}

	public static void setAuthenticated(boolean auth) {
		authenticated = auth;
	}
}
