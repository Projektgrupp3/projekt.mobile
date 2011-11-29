package tddd36.grupp3.views;

import tddd36.grupp3.misc.SplashEvent;
import tddd36.grupp3.resources.Event;
import android.content.Intent;
import android.os.Bundle;

public class MissionGroupActivity extends TabGroupActivity{
	public static TabGroupActivity me;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
		  	me = this;
	        super.onCreate(savedInstanceState);
	        startChildActivity("MissionTabView", new Intent(this,MissionTabView.class));
	    }
	  
	  public static TabGroupActivity getTabParent(){
		  return me;
	  }
}
