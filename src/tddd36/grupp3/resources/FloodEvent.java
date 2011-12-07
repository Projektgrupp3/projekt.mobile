package tddd36.grupp3.resources;

import org.json.JSONException;

import tddd36.grupp3.models.MapModel;

import com.google.android.maps.GeoPoint;

public class FloodEvent extends Event {
	public FloodEvent(GeoPoint gp, String header, String message,
			String eventID, int icon) throws JSONException {
		super(gp, header, message, eventID, icon);
	}
	
	@Override
	public String getObjectDescription(){
		String objectDesc = super.getObjectDescription();
		return objectDesc;
	}

}
