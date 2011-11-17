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

import tddd36.grupp3.models.LoginModel;

public class ConnectionController extends AsyncTask<String,Void,Boolean> implements Observer {

	private static final String COM_IP = "130.236.227.72";
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
	private LoginModel cm;
	private ServerSocket serverSocket;
	private Socket socket;

	private boolean listening = true;
	private boolean readyToSend = false;
	private boolean login = true;
	private static boolean authenticated = false;

	public ConnectionController(LoginModel cm) throws IOException {
		this.cm = cm;
		serverSocket =  new ServerSocket(LISTEN_PORT);
	}

	public void update(Observable observable, Object data) {
		if(data instanceof Boolean){
			authenticated = (Boolean)data;
			if(authenticated)
				listening = true;
			else 
				listening = false;
		}
	}

	public void setLogin(boolean login){
		this.login = login;
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

	public void send(String str){
		establishConnection();
		if(!authenticated){
			pw.println(userName);
			pw.println(password);
		}
		pw.println(str);
		closeConnection();
		readyToSend = false;
	}

	public void login(String userName, String password) throws IOException {
		this.userName = userName;
		this.password = password;

		establishConnection();

		pw.println(userName);
		pw.println(password);

		closeConnection();

	}

	public void setMessageToServer(String msg){
		messageToServer = msg;
		readyToSend = true;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		userName = params[0];
		password = params[1];

		while (listening) {
			if(login){
				Log.d("Auth", "Ej inloggad");
				try {
					login(userName, password);
					Log.d("Auth", "Login försök avslutat");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				login = false;
			}
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
		return false;
	}
}
