package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.models.LoginModel;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionController extends AsyncTask<Void,Void,Boolean> implements Observer {

	private static final String COM_IP = "130.236.227.7";
	private static final int COM_PORT = 4444;
	public static final int LISTEN_PORT = 4445;

	private InputStreamReader isr;
	private PrintWriter pw;
	private BufferedReader br;
	public String serverOutput;
	private JSONObject jsonobject;

	private String messageToServer;
	private String userName;
	private String password; 

	private Socket s;
	private LoginModel cm;
	private ServerSocket serverSocket;
	private Socket socket;

	private boolean listening = true;
	private boolean readyToSend = false;
	private boolean authenticated = false;

	public ConnectionController(LoginModel cm) throws IOException {
		this.cm = cm;
		serverSocket =  new ServerSocket(LISTEN_PORT);

	}

	public void update(Observable observable, Object data) {
		//		if(data instanceof Boolean){
		//			authenticated = (Boolean)data;
		//			if(authenticated)
		//				listening = true;
		//			else 
		//				listening = false;
		//		}
	}
	public void establishConnection(){
		try {
			s = new Socket(COM_IP, COM_PORT);
			pw = new PrintWriter(s.getOutputStream(), true);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void closeConnection(){
		try {
			pw.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String str) throws JSONException{
		this.messageToServer = str;

		readyToSend = true;

		jsonobject = new JSONObject();
		jsonobject.put("user",userName);
		jsonobject.put("pass",password);
		jsonobject.put("msg", messageToServer);

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);

		readyToSend = false;
	}

	public void send(String user, String pass, String message) throws JSONException {
		this.userName = user;
		this.password = pass;
		this.messageToServer = message;

		readyToSend = true;

		jsonobject = new JSONObject();
		jsonobject.put("user",userName);
		jsonobject.put("pass",password);
		jsonobject.put("msg", messageToServer);

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);

		closeConnection();
		readyToSend = false;

	}

	@Override
	protected Boolean doInBackground(Void... params) {

		while (listening) {

			if(readyToSend){
				readyToSend = false;
				Log.d("Login", "Skicka");
				try {
					if(authenticated)
						send(messageToServer);
					else
						send(userName,password,messageToServer);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else {
				Log.d("Loop", "Lyssnar efter inkommande server connections");
				try {				
					socket =  serverSocket.accept();
					new ConnectionTask((socket),this, cm).execute();
				} catch (IOException ioException) {
					ioException.printStackTrace();
					System.exit(-1);
				}
			}
		}
		return false;
	}
}
