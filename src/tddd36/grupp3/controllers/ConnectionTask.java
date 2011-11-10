package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.ClientModel;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionTask extends AsyncTask<Void, Integer, String> implements Observer {

	public static final int LISTEN_PORT = 4445;
	private ClientModel cm;
	private Socket socket = null;
	private boolean listening = true;
	private BufferedReader in;
	private String msg;
	private ConnectionController cc;

	public ConnectionTask(ClientModel cm) {
		this.cm = cm;
	}
	public ConnectionTask(Socket socket, ConnectionController cc, ClientModel cm) {
		this.socket = socket;
		this.cc = cc;
		this.cm = cm;
		Log.d("Initiering", "Connection task skapad");
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
	}

	
	public String listen() throws IOException {
		String input;
		try{
			while(true){
				if((input = in.readLine()) != ""){
					Log.d("Meddelande", input);
					return input;
					}
			}
		} catch(NullPointerException e){
		}
		return null;
	}
	
	protected String doInBackground(Void... params) {
		String message = null;
		try 
		{
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			while(listening){
				Log.d("Do in Backgrounds", "while loop innan androp till meddelande funktion");
				message = listen();
				break;
			}
			cc.setReady(false);
			out.close();
			in.close();
			socket.close();
			Log.d("Avslutar", "Socket stängd");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		cm.executeChange();
		cm.evaluateMessage(result);
		//cm.notifyObservers(result);
		Log.d("Avslutar","Task redo");
	}
}
