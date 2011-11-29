package tddd36.grupp3.views;

import android.content.Intent;
import android.os.Bundle;

public class MissionGroupActivity extends TabGroupActivity{
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        startChildActivity("MissionTabView", new Intent(this,MissionTabView.class));
	    }
}
