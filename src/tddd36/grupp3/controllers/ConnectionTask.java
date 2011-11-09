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
		Log.d("BÖGEN", "är här289789978");
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
	}
	
	public String listen() throws IOException {
		String input;
		try{
			while(true){
				if((input = in.readLine()) != ""){
					Log.d("BÖGEN", "är här1");
					msg = input;
					return msg;
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
				Log.d("BÖGEN", "är här2");
				message = listen();
				break;
			}

			out.close();
			in.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	protected void onPreExecute(String result) {
		cm.executeChange();
		cm.notifyObservers(result);
	}

//	public String listen() throws IOException {
//		try {
//			serverSocket = new ServerSocket(LISTEN_PORT);
//			socket = serverSocket.accept();
//			br = new BufferedReader(new InputStreamReader(socket
//					.getInputStream()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		while (true) {
//			while ((input = br.readLine()) != "") {
//				br.close();
//				socket.close();
//				break;
//			}
//			break;
//		}
//		return input;
//	}
//
//	@Override
//	protected String doInBackground(Void... params) {
//		String str = null;
//		try {
//			str = listen();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return str;
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		cm.executeChange();
//		cm.notifyObservers(result);
//	}
}
