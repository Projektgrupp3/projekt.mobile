package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.ClientModel;
import tddd36.grupp3.views.ClientView;

public class ClientController implements Runnable, Observer {
	
	public ClientController(ClientView cv){
		new ClientModel(cv, this);
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
	public void setUser(String user){
		
	}
	public void setPassword(String password){
		
	}
}
