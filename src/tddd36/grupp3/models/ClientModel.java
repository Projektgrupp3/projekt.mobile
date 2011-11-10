package tddd36.grupp3.models;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ExecutionException;

import tddd36.grupp3.controllers.ClientController;
import tddd36.grupp3.controllers.ConnectionController;
import tddd36.grupp3.controllers.ConnectionTask;
import tddd36.grupp3.views.ClientView;

public class ClientModel extends Observable {

	private String userName;
	private String password;
	private boolean authenticated = false;
	private ConnectionController connectionController;
	private String messageFromServer;
	private ClientView cv;

	public ClientModel(ClientView cv, ClientController cc){
		this.cv = cv;
		try {
			//connectionController = new ConnectionController(this);
			connectionController = new ConnectionController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		addObserver(cv);
		addObserver(cc);
		addObserver(connectionController);
	}

	public void connectToServer(){
		//connectionController.run(userName, password);
		String[] hej = {userName,password};
		connectionController.execute(hej);
		setChanged();
		notifyObservers();
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
