package tddd36.grupp3.views;

import android.content.Intent;
import android.os.Bundle;


public class SIPGroupActivity extends TabGroupActivity{
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        startChildActivity("SIPView", new Intent(this,SIPView.class));
	    }
}
