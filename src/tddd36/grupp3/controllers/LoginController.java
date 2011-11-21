package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.LoginModel;
import tddd36.grupp3.views.LoginView;

public class LoginController implements Runnable, Observer {
	LoginModel cm;

	public LoginController(LoginView cv){
		cm = new LoginModel(cv, this);
	}

	public void run() {
		// TODO Auto-generated method stub
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}

	public void setUserName(String userName){
		cm.setUserName(userName);
	}

	public void setPassword(String password){
		cm.setPassword(password);
	}

	public boolean isAuthenticated(){
		return cm.isAuthenticated();		
	}

	public ConnectionController getConnectionController(){
		return cm.getConnectionController();
	}

}
