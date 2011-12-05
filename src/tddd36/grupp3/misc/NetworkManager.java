package tddd36.grupp3.misc;

import tddd36.grupp3.Sender;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkManager {
	public static Activity activity;
	public static final String WIFI = "NETWORK_WIFI";
	public static final String MOBILE = "NETWORK_MOBILE";
	public static final String NONE = "NETWORK_NONE";
	public static String NETWORK_STATUS;

	public NetworkManager(){
	}
	public NetworkManager(Activity activity){
		NetworkManager.activity = activity;
	}

	public static void chkStatus(Activity activity)
	{
		NetworkManager.activity = activity;
		final ConnectivityManager connMgr = (ConnectivityManager)
		activity.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi =
			connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile =
			connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if( wifi.isAvailable() ){
			NETWORK_STATUS = WIFI;
			Sender.NETWORK_STATUS = WIFI;
		}
		else if( mobile.isAvailable() ){
			NETWORK_STATUS = MOBILE;
			Sender.NETWORK_STATUS = MOBILE;
		}
		else{
			NETWORK_STATUS = NONE;
			Sender.NETWORK_STATUS = NONE;
		}	
	}
}
