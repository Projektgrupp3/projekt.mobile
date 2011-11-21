package tddd36.grupp3.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IncomingCallReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent myIntent = new Intent(context, IncomingCall.class);
		myIntent.putExtra("intent", intent);
		context.startActivity(myIntent);
	}

}
