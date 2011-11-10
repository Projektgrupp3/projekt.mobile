package tddd36.grupp3.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.R;
import tddd36.grupp3.models.EventModel;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EventView extends Activity implements Observer{
	
	EventModel eventmodel;
	TextView eventheader, eventdescription, eventaddress;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.eventtablayout);
	eventheader = (TextView)findViewById(R.id.eventheader2);
	eventdescription = (TextView)findViewById(R.id.eventdescription2);
	eventaddress = (TextView)findViewById(R.id.eventaddress2);
	
	eventmodel = new EventModel(this,"Trafikolycka", "Större olycka..", "Valla-rondellen", 
					new SimpleDateFormat("HH:mm:ss").format(new Date()),5);
	eventheader.set
}

public void update(Observable observable, Object data) {
	// TODO Auto-generated method stub
	
}
}
