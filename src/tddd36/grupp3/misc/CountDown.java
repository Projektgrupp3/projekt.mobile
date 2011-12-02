package tddd36.grupp3.misc;

import java.util.Observable;


public class CountDown extends Observable implements Runnable{

	private int countdown = 10000;
	private boolean isRunning;
	
	public void run() {
		startRunning();
		try {
			while(countdown>0 && isRunning){
				setChanged();
				Thread.sleep(1000);
				countdown -= 1000;
				notifyObservers(""+(countdown/1000));
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

}