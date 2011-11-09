package tddd36.grupp3.models;

import java.util.ArrayList;
import java.util.Observable;

import tddd36.grupp3.controllers.UnitController;
import tddd36.grupp3.views.UnitView;

public class UnitModel extends Observable {
	
	ArrayList<String> allUnits = new ArrayList<String>();
	
	public UnitModel(UnitView uv, UnitController uc) {
		addObserver(uv);
		addObserver(uc);
	}

}
