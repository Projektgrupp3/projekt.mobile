package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.ClientModel;
import tddd36.grupp3.views.ClientView;

public class ClientController implements Runnable, Observer {
	ClientModel cm;
<<<<<<< HEAD
	
=======

>>>>>>> davve
	public ClientController(ClientView cv){
		cm = new ClientModel(cv, this);
	}

	public void run() {
		// TODO Auto-generated method stub
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		
	}
	
	public void setUserName(String userName){
		cm.setUserName(userName);
	}
	
=======

	}

	public void setUserName(String userName){
		cm.setUserName(userName);
	}

>>>>>>> davve
	public void setPassword(String password){
		cm.setPassword(password);
	}
	public void connectToServer(){
		cm.connectToServer();
	}
	public boolean isAuthenticated(){
		return cm.isAuthenticated();		
	}
<<<<<<< HEAD
	
=======
//	public void startListen(){
//		cm.listen();
//	}

>>>>>>> davve
}