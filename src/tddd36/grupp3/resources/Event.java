package tddd36.grupp3.resources;

import tddd36.grupp3.R;

import com.google.android.maps.GeoPoint;

public class Event extends MapObject{
	private String time;
	
	public Event(GeoPoint gp, String header, String message, String time){
		super(gp, header, message, R.drawable.event_icon);
		this.time = time;
	}
	public String getTime(){
		return time;
	}
	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		return objectDesc+"Tid: "+time;

	}

}
