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

import android.os.AsyncTask;

import android.util.Log;

import tddd36.grupp3.models.ClientModel;

public class ConnectionController extends AsyncTask<String,Void,Void> implements Observer {

	private static final String COM_IP = "130.236.226.149";
	private static final int COM_PORT = 4444;
	public static final int LISTEN_PORT = 4445;

	private InputStreamReader isr;
	private PrintWriter pw;
	private BufferedReader br;
	public String serverOutput;

	private String messageToServer;
	private String userName;
	private String password; 
	
	private Socket s;
	private ClientModel cm;
	private ServerSocket serverSocket;
	private Socket socket;
	
	private boolean listening = true;
	private boolean ready = false;
	private boolean readyToSend = false;


	public ConnectionController(ClientModel cm) throws IOException {
		this.cm = cm;
		serverSocket =  new ServerSocket(LISTEN_PORT);
	}

	public void update(Observable observable, Object data) {
	}
	
	public void establishConnection(){
		try {
			s = new Socket(COM_IP, COM_PORT);
			isr = new InputStreamReader(s.getInputStream());
			pw = new PrintWriter(s.getOutputStream(), true);
			br = new BufferedReader(isr);
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
			br.close();
			isr.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(String str){
		establishConnection();
		try {
			pw.println(userName);
			pw.println(password);

			if ((serverOutput = br.readLine()) != "") {
				if (serverOutput.equals("Authenticated")) {
					cm.setAuthenticated(true);
					pw.println(str);
					closeConnection();
					readyToSend = false;
				} else {
					cm.setAuthenticated(false);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void login(String userName, String password) throws IOException {
		this.userName = userName;
		this.password = password;
		establishConnection();

		
		if (!cm.isAuthenticated()) {
			try {
				pw.println(userName);
				pw.println(password);

				if ((serverOutput = br.readLine()) != "") {
					if (serverOutput.equals("Authenticated")) {
						Log.d("Sp�rutskrift",serverOutput);
						cm.setAuthenticated(true);
					} else {
						cm.setAuthenticated(false);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		closeConnection();
	}

	public void setReady(boolean ready){
		this.ready = ready;
	}

	@Override
	protected Void doInBackground(String... params) {
		if(!cm.isAuthenticated()){
			try {
				login(params[0], params[1]);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (listening) {
			Log.d("Loop", "Lyssnar efter inkommande server connections");
			try {				
				socket =  serverSocket.accept();
				new ConnectionTask((socket),this, cm).execute();

			} catch (IOException ioException) {
				ioException.printStackTrace();
				System.exit(-1);
			} 
			//send metod
			if(readyToSend){
				send(messageToServer);
			}
		}
		try {
			while(!ready){
				Log.d("Loop", "V�ntar p� async task");
			}
			serverSocket.close();
			Log.d("Avslutar","ServerSocket socket st�ngd.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}
}
