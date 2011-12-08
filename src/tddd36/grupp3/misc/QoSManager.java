package tddd36.grupp3.misc;

import android.app.Activity;
import android.view.WindowManager;
import android.widget.Toast;

public class QoSManager{

	private static int power;
	public static WindowManager.LayoutParams lp = null;
	public static Activity activity;

	public static String BATTERY_LEVEL = "GOOD";

	public static String VERY_LOW = "VERY_LOW";
	public static String LOW = "LOW";
	public static String MEDIUM = "MEDIUM";
	public static String HIGH = "HIGH";

	public static void setActivity(Activity ac){
		activity = ac;
	}
	public static Activity getActivity(){
		return activity;
	}

	public static void setScreenBrightness(int powerleft){
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		power = powerleft;

		if(power<=5){
			lp.screenBrightness = 0.05f;
			activity.getWindow().setAttributes(lp);
			BATTERY_LEVEL = VERY_LOW;
			Toast.makeText(activity,"Väldigt låg batterinivå",Toast.LENGTH_LONG).show();
		}

		else if(power<=10 && power >5){
			lp.screenBrightness = 0.2f;
			activity.getWindow().setAttributes(lp);
			BATTERY_LEVEL = LOW;
			Toast.makeText(activity,"Låg batterinivå",Toast.LENGTH_LONG).show();
		}
		else if(power>10 && power <=25){
			lp.screenBrightness = 0.3f;
			activity.getWindow().setAttributes(lp);
			BATTERY_LEVEL = MEDIUM;
			Toast.makeText(activity,"Ok batterinivå",Toast.LENGTH_LONG).show();
		}
		else if(power> 25){
			lp.screenBrightness = 0.7f;
			activity.getWindow().setAttributes(lp);
			BATTERY_LEVEL = HIGH;
			Toast.makeText(activity,"Hög batterinivå",Toast.LENGTH_LONG).show();
		}
	}
}

