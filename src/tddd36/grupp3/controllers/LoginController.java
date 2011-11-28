package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.LoginModel;
import tddd36.grupp3.views.LoginView;

public class LoginController implements Runnable, Observer {
	LoginModel loginmodel;

	public LoginController(LoginView cv){
		loginmodel = new LoginModel(cv, this);
	}

	public void run() {
		// TODO Auto-generated method stub
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}

	public void setUserName(String userName){
		loginmodel.setUserName(userName);
	}

	public void setPassword(String password){
		loginmodel.setPassword(password);
	}

	public boolean isAuthenticated(){
		return loginmodel.isAuthenticated();		
	}

}
