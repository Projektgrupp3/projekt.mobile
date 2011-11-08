package tddd36.grupp3.models;

import java.util.Observable;

import tddd36.grupp3.controllers.ClientController;
import tddd36.grupp3.views.ClientView;

public class ClientModel extends Observable{
	
	private static String userName;
	private static String password;
	private static boolean authenticated;
	
	public ClientModel(ClientView cv, ClientController cc){
		addObserver(cv);
		addObserver(cc);
	}

	public String getUserName() {
		return userName;
	}

	public static void setUserName(String name) {
		userName = name;
	}

	public String getPassword() {
		return password;
	}

	public static void setPassword(String pass) {
		pass = password;
	}

	public static boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
}
