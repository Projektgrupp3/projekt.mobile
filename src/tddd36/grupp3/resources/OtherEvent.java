package tddd36.grupp3.resources;

import org.json.JSONException;

import com.google.android.maps.GeoPoint;

public class OtherEvent extends Event{

	public OtherEvent(GeoPoint gp, String header, String message,
			String eventID, int icon) throws JSONException {
		super(gp, header, message, eventID, icon);
	}

}
