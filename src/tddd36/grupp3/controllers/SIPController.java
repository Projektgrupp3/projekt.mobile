package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.SIPModel;
import tddd36.grupp3.views.SIPView;

public class SIPController implements Observer, Runnable{
	
	SIPModel sipmodel;
	Thread thread = new Thread(this);
	
	public SIPController(SIPView sipview){
		sipmodel = new SIPModel(sipview, this);
		thread.run();
	}

	public SIPModel getSIPModel(){
		return sipmodel;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
}
