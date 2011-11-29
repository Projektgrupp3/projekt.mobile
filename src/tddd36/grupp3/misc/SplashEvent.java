package tddd36.grupp3.misc;

import tddd36.grupp3.resources.Event;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.content.DialogInterface.OnClickListener;

public class SplashEvent extends Thread implements OnClickListener {
	private Context c;
	private Event ev;
	private int countdown;
	private boolean isRunning;

	private AlertDialog.Builder builder;
	private AlertDialog alertForEvent;

	public SplashEvent(Context c, Event ev){
		this.ev = ev;
		this.c = c;
		countdown = 900000; //90 sekunder
	}
	public void run() {
		startRunning();
		builder = new AlertDialog.Builder(c);
		alertForEvent = builder.create();
		alertForEvent.setTitle("Inkommande larm!");
		alertForEvent.setMessage("Du har: "+countdown+ " sekunder på dig att svara.");
		builder.setPositiveButton("Kvittera", this);
		builder.setNegativeButton("Neka", this);
		try {
			while(countdown>0 && isRunning){
				alertForEvent.setMessage("Du har: "+countdown+ " sekunder på dig att svara.");
				sleep(1000);
				countdown -= 1000;
			}
			stopRunning();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void stopRunning(){
		isRunning = false;
	}
	public void startRunning(){
		isRunning = true;
	}

	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case 0: stopRunning();
			return;
		case 1: 
			return;
		}
	}

}
