package tddd36.grupp3.misc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

public class QoSManager{
	String powerLeft;
	int power;

	//	WindowManager.LayoutParams lp = getWindow().getAttributes();
	//		this.registerReceiver(this.myBatteryReceiver,
	//				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

	public static WindowManager.LayoutParams lp = null;
	public Activity activity;
	public static BroadcastReceiver myBatteryReceiver;

	public QoSManager(final WindowManager.LayoutParams lp, final Activity activity){
		this.lp = lp;
		this.activity = activity;
		myBatteryReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub

				if (arg1.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
					powerLeft = String.valueOf(arg1.getIntExtra("level",0));

					power = Integer.parseInt(powerLeft);

				}
				if(power<=5){
					lp.screenBrightness = 0.05f;
					activity.getWindow().setAttributes(lp);
				}

				if(power<=10 && power <5){
					lp.screenBrightness = 0.2f;
					activity.getWindow().setAttributes(lp);
				}
				if(power>10 && power <=25){
					lp.screenBrightness = 0.3f;
					activity.getWindow().setAttributes(lp);
				}
				if(power>25){
					lp.screenBrightness = 0.7f;
					activity.getWindow().setAttributes(lp);
				}
			}
		};
	}
}


