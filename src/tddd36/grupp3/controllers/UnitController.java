package tddd36.grupp3.controllers;

import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.UnitModel;
import tddd36.grupp3.views.UnitView;

public class UnitController implements Runnable, Observer {

	private UnitView uv;
	private UnitModel um;
	
	public UnitController(UnitView uv){
		this.uv = uv;	
		um = new UnitModel(uv, this);
	}

	public void run() {
		// TODO Auto-generated method stub

	}

	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
